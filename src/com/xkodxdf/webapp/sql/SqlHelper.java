package com.xkodxdf.webapp.sql;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public <T> T exucuteStatement(String query, SqlExecutor<T> ex) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return ex.execute(ps);
        } catch (SQLException e) {
            String duplicateErrCode = "23505";
            if (e.getSQLState().equals(duplicateErrCode)) {
                throw new ExistStorageException(null);
            }
            throw new StorageException(e);
        }
    }
}
