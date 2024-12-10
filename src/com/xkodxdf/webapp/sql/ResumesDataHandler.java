package com.xkodxdf.webapp.sql;

import com.xkodxdf.webapp.model.Resume;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResumesDataHandler {

    void attach(Resume r, ResultSet rs) throws SQLException;
}
