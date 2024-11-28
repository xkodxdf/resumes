package com.xkodxdf.webapp.sql;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                throw new ExistStorageException(extractUUID(e.getMessage()));
            }
            throw new StorageException(e);
        }
    }

    private static String extractUUID(String errorMessage) {
        Pattern pattern = Pattern.compile("\\(uuid\\)=\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(errorMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
