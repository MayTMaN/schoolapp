package com.organization.schoolapp.service;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.TeacherRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherService implements TeacherRepository {
    private final DatabaseConnection dbConnection;

    public TeacherService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Teacher teacher) {
        String sql = "INSERT INTO teachers (name) VALUES (?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, teacher.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int teacherId) {
        String sql = "DELETE FROM teachers WHERE teacher_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, teacherId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Teacher findById(int teacherId) {
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,teacherId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("name"));
                return teacher;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Teacher> findAll() {
        String sql = "SELECT * FROM teachers";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            List<Teacher> teachers = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("name"));
                teachers.add(teacher);
            }

            return teachers;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
