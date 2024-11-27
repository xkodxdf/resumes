package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.Config;
import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.sql.SqlHelper;
import com.xkodxdf.webapp.sql.SqlQueries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage() {
        this.sqlHelper = new SqlHelper(Config.get().getDbUrl(), Config.get().getDbUsr(), Config.get().getDbPwd());
    }

    @Override
    public int size() {
        return sqlHelper.exucuteStatement(SqlQueries.SIZE, ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.exucuteStatement(SqlQueries.SAVE, ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.exucuteStatement(SqlQueries.GET, ps -> {
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
        sqlHelper.exucuteStatement(SqlQueries.UPDATE, ps -> {
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
        int executeResult = sqlHelper.exucuteStatement(SqlQueries.DELETE, ps -> {
            ps.setString(1, uuid);
            return ps.executeUpdate();
        });
        if (executeResult == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.exucuteStatement(SqlQueries.GET_ALL, ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            Collections.sort(ret);
            return ret;
        });
    }

    @Override
    public void clear() {
        sqlHelper.exucuteStatement(SqlQueries.CLEAR, PreparedStatement::execute);
    }
}
