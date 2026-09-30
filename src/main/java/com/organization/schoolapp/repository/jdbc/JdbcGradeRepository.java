package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Grade;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.GradeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcGradeRepository implements GradeRepository {
    private final DatabaseConnection dbConnection;

    public JdbcGradeRepository(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void setGrade(double grade, int courseId, int studentId, LocalDate date) {
        String sql = "INSERT INTO grades (grade, course_id, student_id, date) VALUES (?,?,?,?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, grade);
            statement.setInt(2,courseId);
            statement.setInt(3,studentId);
            statement.setObject(4,date);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void updateGrade(int courseId, int studentId, LocalDate date, double newGrade) {
        String sql = "UPDATE grades SET grade = ? WHERE course_id = ? AND student_id = ? AND date = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1,newGrade);
            statement.setInt(2,courseId);
            statement.setInt(3,studentId);
            statement.setObject(4, date);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeGrade(int courseId, int studentId, LocalDate date) {
        String sql = "DELETE FROM grades WHERE course_id = ? AND student_id = ? AND date = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1,courseId);
            statement.setInt(2,studentId);
            statement.setObject(3,date);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Grade findGrade(int courseId, int studentId, LocalDate date) {
        String sql = "SELECT grades.grade, grades.date,\n" +
        "students.student_id, students.name AS student_name,\n" +
        "courses.course_id, courses.course_name,\n" +
        "teachers.teacher_id, teachers.name AS teacher_name\n" +
        "FROM grades\n" +
        "JOIN students ON grades.student_id = students.student_id\n" +
        "JOIN courses  ON grades.course_id  = courses.course_id\n" +
        "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
        "WHERE grades.course_id = ? AND grades.student_id = ? AND grades.date = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,courseId);
            statement.setInt(2,studentId);
            statement.setObject(3,date);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
                Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                Grade grade = new Grade(student,course,resultSet.getDouble("setGrade"), resultSet.getObject("date", LocalDate.class));
                return grade;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Grade> findStudentGrades(int courseId, int studentId) {
        String sql = "SELECT grades.grade, grades.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM grades\n" +
                "JOIN students ON grades.student_id = students.student_id\n" +
                "JOIN courses  ON grades.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE grades.course_id = ? AND grades.student_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);
            statement.setInt(2, studentId);
            ResultSet resultSet = statement.executeQuery();
            List<Grade> grades = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
                Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                Grade grade = new Grade(student,course,resultSet.getDouble("setGrade"), resultSet.getObject("date", LocalDate.class));
                grades.add(grade);
            }

            return grades;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Grade> findGradesByCourse(int courseId) {
        String sql = "SELECT grades.grade, grades.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM grades\n" +
                "JOIN students ON grades.student_id = students.student_id\n" +
                "JOIN courses  ON grades.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE grades.course_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            List<Grade> grades = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
                Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                Grade grade = new Grade(student,course,resultSet.getDouble("setGrade"), resultSet.getObject("date", LocalDate.class));
                grades.add(grade);
            }

            return grades;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
