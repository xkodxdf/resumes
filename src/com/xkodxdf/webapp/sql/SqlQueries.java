package com.xkodxdf.webapp.sql;

public enum SqlQueries {
    SIZE("SELECT count(*) FROM resume"),
    SAVE("INSERT INTO resume (uuid, full_name) VALUES(?,?)"),
    GET("SELECT * FROM resume WHERE uuid=?"),
    UPDATE("UPDATE resume SET full_name=? WHERE uuid=?"),
    DELETE("DELETE FROM resume WHERE uuid=?"),
    GET_ALL("SELECT * FROM resume"),
    CLEAR("DELETE FROM resume");

    SqlQueries(String query) {
        this.query = query;
    }

    private final String query;

    public String get() {
        return query;
    }
}
