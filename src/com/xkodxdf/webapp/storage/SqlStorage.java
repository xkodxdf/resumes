package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.ContactType;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                    insertContacts(conn, r);
                    return null;
                }
        );
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
                        do {
                            String type = rs.getString("type");
                            String value = rs.getString("value");
                            if (!(type == null || value == null)) {
                                ret.addContact(ContactType.valueOf(type), value);
                            }
                        } while (rs.next());
                        return ret;
                    }
                });
    }

    @Override
    public void update(Resume r) {
        delete(r.getUuid());
        save(r);
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
                while (rs.next()) {
                    ret.put(rs.getString("uuid"),
                            new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
            }
            try (Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                 ResultSet rs = statement.executeQuery(
                         "SELECT resume_uuid, type, value FROM contact " +
                                 "ORDER BY resume_uuid")) {
                while (rs.next()) {
                    String currentUuid = rs.getString("resume_uuid");
                    Resume resume = ret.get(currentUuid);
                    if (resume == null) {
                        continue;
                    }
                    do {
                        resume.addContact(ContactType.valueOf(rs.getString("type")),
                                rs.getString("value"));
                    } while (rs.next() && currentUuid.equals(rs.getString("resume_uuid")));
                    rs.previous();
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

    private void insertContacts(Connection conn, Resume r) {
        sqlHelper.executeTransaction(connection -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO contact (resume_uuid, type, value) " +
                            "VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> contacts : r.getContacts().entrySet()) {
                    prepareStatement(ps, r.getUuid(), contacts.getKey().name(), contacts.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }
}
