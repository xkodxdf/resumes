package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.ContactType;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                    saveOrUpdateContacts(conn, r, "INSERT INTO contact (resume_uuid, type, value) " +
                            "VALUES (?,?,?)");
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
                        return getResume(rs, uuid);
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
            saveOrUpdateContacts(conn, r, "UPDATE contact SET value=? WHERE resume_uuid=? AND type=?");
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
            List<Resume> ret = new ArrayList<>();
            try (Statement statement = conn.createStatement();
                 ResultSet rs = statement.executeQuery(
                         "SELECT * FROM resume " +
                                 "ORDER BY full_name, uuid")) {
                while (rs.next()) {
                    ret.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
            }
            Map<String, Map<ContactType, String>> allContacts = new HashMap<>();
            try (Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                 ResultSet rs = statement.executeQuery(
                         "SELECT resume_uuid, type, value FROM contact " +
                                 "ORDER BY resume_uuid")) {
                while (rs.next()) {
                    String currentUuid = rs.getString("resume_uuid");
                    Map<ContactType, String> currentUuidContacts = new HashMap<>();
                    do {
                        currentUuidContacts.put(ContactType.valueOf(rs.getString("type")),
                                rs.getString("value"));
                    } while (rs.next() && currentUuid.equals(rs.getString("resume_uuid")));
                    rs.previous();
                    allContacts.put(currentUuid, currentUuidContacts);
                }
            }
            ret = ret.stream().peek(r -> {
                for (Map.Entry<ContactType, String> c : allContacts.get(r.getUuid()).entrySet()) {
                    r.addContact(c.getKey(), c.getValue());
                }
            }).collect(Collectors.toList());
            return ret;
        });
    }

    @Override
    public void clear() {
        sqlHelper.executeStatement("DELETE FROM resume", PreparedStatement::execute);
    }

    private Resume getResume(ResultSet rs, String uuid) throws SQLException {
        Resume ret = new Resume(uuid, rs.getString("full_name"));
        do {
            ContactType type = ContactType.valueOf(rs.getString("type"));
            String value = rs.getString("value");
            ret.addContact(type, value);
        } while (rs.next());
        return ret;
    }

    private void prepareStatement(PreparedStatement ps, String... params) throws SQLException {
        for (int i = 1; i <= params.length; i++) {
            ps.setString(i, params[i - 1]);
        }
    }

    private void saveOrUpdateContacts(Connection conn, Resume r, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (Map.Entry<ContactType, String> contacts : r.getContacts().entrySet()) {
                if (query.startsWith("INSERT")) {
                    prepareStatement(ps, r.getUuid(), contacts.getKey().name(), contacts.getValue());
                } else {
                    prepareStatement(ps, contacts.getValue(), r.getUuid(), contacts.getKey().name());
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
