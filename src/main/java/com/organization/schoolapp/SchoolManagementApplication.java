package com.organization.schoolapp;

import com.organization.schoolapp.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SchoolManagementApplication {
    static void main() {
        DatabaseConnection dbConnection = new DatabaseConnection();

        try (Connection connection = dbConnection.getConnection()) {
            System.out.println("You've successfully connected!");
        } catch (SQLException e) {
            System.out.println("Connection failed.");
        }
    }
}
