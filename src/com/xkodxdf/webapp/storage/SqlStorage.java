package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.Config;
import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.sql.SqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage() {
        this.sqlHelper = new SqlHelper(Config.get().getDbUrl(), Config.get().getDbUsr(), Config.get().getDbPwd());
    }

    @Override
    public int size() {
        return sqlHelper.exucuteStatement("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.exucuteStatement("INSERT INTO resume (uuid, full_name) VALUES(?,?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            String duplicateErrCode = "23505";
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                if (e.getSQLState().equals(duplicateErrCode)) {
                    throw new ExistStorageException(r.getUuid());
                }
                throw new SQLException(e);
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.exucuteStatement("SELECT * FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(rs.getString("uuid").trim(), rs.getString("full_name"));
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.exucuteStatement("UPDATE resume SET full_name=? WHERE uuid=?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.exucuteStatement("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.exucuteStatement("SELECT * FROM resume ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return ret;
        });
    }

    @Override
    public void clear() {
        sqlHelper.exucuteStatement("DELETE FROM resume", PreparedStatement::execute);
    }
}
