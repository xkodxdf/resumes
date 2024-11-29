package com.xkodxdf.webapp.sql;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {
            String duplicateErrCode = "23505";
            if (e.getSQLState().equals(duplicateErrCode)) {
                return new ExistStorageException(extractUUID(e.getMessage()));
            }
        }
        return new StorageException(e);
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
