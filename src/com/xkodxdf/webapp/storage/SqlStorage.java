package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.*;
import com.xkodxdf.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public int size() {
        return sqlHelper.executeStatement(
                "SELECT count(*) " +
                        "FROM resume", ps -> {
                    try (ResultSet rs = ps.executeQuery()) {
                        return rs.next() ? rs.getInt(1) : 0;
                    }
                });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO resume (uuid, full_name) " +
                            "VALUES (?,?)")) {
                prepareStatement(ps, r.getUuid(), r.getFullName());
                ps.execute();
            }
            insertContacts(r, conn);
            insertSections(r, conn);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeTransaction(conn -> {
            Resume ret;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM resume r " +
                            "LEFT JOIN contact c " +
                            "ON r.uuid = c.resume_uuid " +
                            "WHERE r.uuid =?")) {
                ps.setString(1, uuid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    ret = new Resume(uuid, rs.getString("full_name"));
                    String contactType;
                    do {
                        contactType = rs.getString("type");
                        if (contactType != null) {
                            ret.addContact(ContactType.valueOf(contactType), rs.getString("value"));
                        }
                    } while (rs.next());
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT type, value FROM section " +
                            "WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return ret;
                    }
                    do {
                        addSection(ret, rs);
                    } while (rs.next());
                }
            }
            return ret;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE resume " +
                            "SET full_name=? " +
                            "WHERE uuid=?")) {
                prepareStatement(ps, r.getFullName(), r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM contact " +
                            "WHERE resume_uuid=?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM section " +
                            "WHERE resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertContacts(r, conn);
            insertSections(r, conn);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeStatement(
                "DELETE FROM resume " +
                        "WHERE uuid=?", ps -> {
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeTransaction(conn -> {
            Map<String, Resume> ret = new LinkedHashMap<>();
            try (Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(
                         "SELECT * FROM resume " +
                                 "ORDER BY full_name, uuid")) {
                String uuid;
                while (rs.next()) {
                    uuid = rs.getString("uuid");
                    ret.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            attachContacts(ret, conn);
            attachSections(ret, conn);
            return new ArrayList<>(ret.values());
        });
    }

    @Override
    public void clear() {
        sqlHelper.executeStatement("DELETE FROM resume", PreparedStatement::execute);
    }

    private void prepareStatement(PreparedStatement ps, String... params) throws SQLException {
        for (int i = 1; i <= params.length; i++) {
            ps.setString(i, params[i - 1]);
        }
    }

    private void insertSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO contact (resume_uuid, type, value) " +
                        "VALUES(?,?,?)")) {
            for (Map.Entry<ContactType, String> contacts : r.getContacts().entrySet()) {
                prepareStatement(ps, r.getUuid(), contacts.getKey().name(), contacts.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO section (resume_uuid, type, value) " +
                        "VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> sections : r.getSections().entrySet()) {
                if (sections.getValue() instanceof TextSection) {
                    prepareStatement(ps, r.getUuid(), sections.getKey().name(), sections.getValue().toString());
                } else {
                    prepareStatement(ps, r.getUuid(), sections.getKey().name(),
                            String.join("\n", ((ListSection) sections.getValue()).getContent()));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void attachContacts(Map<String, Resume> resumes, Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT resume_uuid, type, value " +
                             "FROM contact " +
                             "ORDER BY resume_uuid")) {
            String uuid;
            String currentUuid = null;
            Resume resume = null;
            while (rs.next()) {
                uuid = rs.getString("resume_uuid");
                if (!Objects.equals(uuid, currentUuid)) {
                    currentUuid = uuid;
                    resume = resumes.get(currentUuid);
                }
                resume.addContact(ContactType.valueOf(rs.getString("type")),
                        rs.getString("value"));
            }
        }
    }

    private void attachSections(Map<String, Resume> resumes, Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT resume_uuid, type, value " +
                             "FROM section " +
                             "ORDER BY resume_uuid")) {
            String uuid;
            String currentUuid = null;
            Resume resume = null;
            while (rs.next()) {
                uuid = rs.getString("resume_uuid");
                if (!Objects.equals(uuid, currentUuid)) {
                    currentUuid = uuid;
                    resume = resumes.get(currentUuid);
                }
                addSection(resume, rs);
            }
        }
    }

    private void addSection(Resume resume, ResultSet resultSet) throws SQLException {
        String typeName;
        SectionType type;
        typeName = resultSet.getString("type");
        List<String> content;
        if (typeName != null) {
            type = SectionType.valueOf(typeName);
            switch (type) {
                case OBJECTIVE:
                case PERSONAL:
                    resume.addSection(type, new TextSection(resultSet.getString("value")));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    content = Arrays.asList(resultSet.getString("value").split("\n"));
                    resume.addSection(type, new ListSection(content));
            }
        }
    }
}
