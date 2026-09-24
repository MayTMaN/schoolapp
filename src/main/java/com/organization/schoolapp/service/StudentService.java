package com.organization.schoolapp.service;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.StudentRepository;

import javax.swing.text.html.HTMLDocument;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentService implements StudentRepository {
    private final DatabaseConnection dbConnection;

    public StudentService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Student student) {
        String sql = "INSERT INTO students (name) VALUES (?)";

        try (Connection connection = dbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = dbConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student findById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,studentId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("name"));
                return student;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM students";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("name"));
                students.add(student);
            }

            return students;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
