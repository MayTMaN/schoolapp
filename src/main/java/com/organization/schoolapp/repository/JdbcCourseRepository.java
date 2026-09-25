package com.organization.schoolapp.repository;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcCourseRepository implements CourseRepository {
    private final DatabaseConnection dbConnection;

    public JdbcCourseRepository(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public void save(Course course) {
        String sql = "INSERT INTO courses (course_name, teacher_id) VALUES (?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, course.getCourseName());
            statement.setInt(2, course.getTeacher().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1,courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course course) {
        String sql = "UPDATE courses SET course_name = ?, teacher_id = ? WHERE course_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, course.getCourseName());
            statement.setInt(2, course.getTeacher().getId());
            statement.setInt(3,course.getCourseId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Course findById(int courseId) {
        String sql = "SELECT courses.course_id, courses.course_name, teachers.teacher_id, teachers.name\n" +
                "FROM courses\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE courses.course_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,courseId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("name"));
                Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                return course;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Course> findAll() {
        String sql = "SELECT * FROM courses JOIN teachers ON courses.teacher_id = teachers.teacher_id";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            List<Course> courses = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("name"));
                Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                courses.add(course);
            }

            return courses;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
