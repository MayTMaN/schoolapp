package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.EnrollmentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcEnrollmentRepository implements EnrollmentRepository {
    private final DatabaseConnection dbConnection;

    public JdbcEnrollmentRepository(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void enroll(int courseId, int studentId) {
        String sql = "INSERT INTO enrollments (course_id, student_id) VALUES (?,?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);
            statement.setInt(2, studentId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss(int courseId, int studentId) {
        String sql = "DELETE FROM enrollments WHERE course_id = ? AND student_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);
            statement.setInt(2, studentId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isEnrolled(int courseId, int studentId) {
        String sql = "SELECT 1 FROM enrollments WHERE course_id = ? AND student_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.setInt(2, studentId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Student> findStudentsByCourse(int courseId) {
        String sql = "SELECT students.student_id, students.name\n" +
                "FROM enrollments\n" +
                "JOIN students ON enrollments.student_id = students.student_id\n" +
                "WHERE enrollments.course_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            List<Student> enrolledStudents = new ArrayList<>();
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("name"));
                enrolledStudents.add(student);
            }

            return enrolledStudents;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
