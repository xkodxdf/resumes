package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.ContactType;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
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
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeStatement(
                "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new NotExistStorageException(uuid);
                        }
                        Resume ret = new Resume(uuid, rs.getString("full_name"));
                        String type;
                        do {
                            type = rs.getString("type");
                            if (type != null) {
                                ret.addContact(ContactType.valueOf(type), rs.getString("value"));
                            }
                        } while (rs.next());
                        return ret;
                    }
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertContacts(r, conn);
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
            try (Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(
                         "SELECT resume_uuid, type, value FROM contact " +
                                 "ORDER BY resume_uuid")) {
                String uuid;
                String currentUuid = null;
                Resume resume = null;
                while (rs.next()) {
                    uuid = rs.getString("resume_uuid");
                    if (!Objects.equals(uuid, currentUuid)) {
                        currentUuid = uuid;
                        resume = ret.get(currentUuid);
                    }
                    resume.addContact(ContactType.valueOf(rs.getString("type")),
                            rs.getString("value"));
                }
            }
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

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO contact (resume_uuid, type, value) " +
                        "VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> contacts : r.getContacts().entrySet()) {
                prepareStatement(ps, r.getUuid(), contacts.getKey().name(), contacts.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
