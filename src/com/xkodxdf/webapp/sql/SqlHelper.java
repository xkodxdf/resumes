package com.xkodxdf.webapp.sql;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.exception.StorageException;

import java.sql.*;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public <T> T exucuteStatement(SqlQueries query, SqlExecutor<T> ex) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query.get())) {
            return ex.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        } catch (ExistStorageException e) {
            throw new ExistStorageException(e.getUuid());
        } catch (NotExistStorageException e) {
            throw new NotExistStorageException(e.getUuid());
        }
    }
}
