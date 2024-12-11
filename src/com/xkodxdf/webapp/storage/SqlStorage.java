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
                    ResultSet rs = ps.executeQuery();
                    return rs.next() ? rs.getInt(1) : 0;
                });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO resume (uuid, full_name) " +
                            "VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
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
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM resume r " +
                            "WHERE r.uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));

            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT type, value FROM contact " +
                            "WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String contactType = rs.getString("type");
                    if (contactType != null) {
                        resume.addContact(ContactType.valueOf(contactType), rs.getString("value"));
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT type, value FROM section " +
                            "WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(resume, rs);
                }
            }
            return resume;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE resume " +
                            "SET full_name=? " +
                            "WHERE uuid=?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
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
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(
                         "SELECT * FROM resume " +
                                 "ORDER BY full_name, uuid")) {
                String uuid;
                while (rs.next()) {
                    uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            addContacts(resumes, conn);
            addSections(resumes, conn);
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public void clear() {
        sqlHelper.executeStatement("DELETE FROM resume", PreparedStatement::execute);
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO contact (resume_uuid, type, value) " +
                        "VALUES(?,?,?)")) {
            for (Map.Entry<ContactType, String> contacts : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, contacts.getKey().name());
                ps.setString(3, contacts.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO section (resume_uuid, type, value) " +
                        "VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> sections : r.getSections().entrySet()) {
                SectionType sectionType = sections.getKey();
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        ps.setString(1, r.getUuid());
                        ps.setString(2, sections.getKey().name());
                        ps.setString(3, sections.getValue().toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ps.setString(1, r.getUuid());
                        ps.setString(2, sections.getKey().name());
                        ps.setString(3, String.join("\n",
                                ((ListSection) sections.getValue()).getContent()));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addContacts(Map<String, Resume> resumes, Connection conn) throws SQLException {
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

    private void addSections(Map<String, Resume> resumes, Connection conn) throws SQLException {
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
        String typeName = resultSet.getString("type");
        if (typeName != null) {
            SectionType type = SectionType.valueOf(typeName);
            switch (type) {
                case OBJECTIVE:
                case PERSONAL:
                    resume.addSection(type, new TextSection(resultSet.getString("value")));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    List<String> content = Arrays.asList(resultSet.getString("value").split("\n"));
                    resume.addSection(type, new ListSection(content));
            }
        }
    }
}
