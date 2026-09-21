package com.organization.schoolapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    private static final String URL = "jdbc:postgresql://localhost:5432/School";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "0000";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

}
