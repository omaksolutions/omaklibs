package com.omak.omakhelpers.contactUtil;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public interface ResultSetHelper {
    String[] getColumnNames(ResultSet rs) throws SQLException;

    String[] getColumnValues(ResultSet rs) throws SQLException, IOException;
}
