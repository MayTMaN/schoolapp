package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Attendance;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.AttendanceRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcAttendanceRepository implements AttendanceRepository {
    private final DatabaseConnection dbConnection;

    public JdbcAttendanceRepository(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(boolean isPresent, int courseId, int studentId, LocalDate date) {
        String sql = "INSERT INTO attendance (is_present, course_id, student_id, date) VALUES (?,?,?,?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1,isPresent);
            statement.setInt(2,courseId);
            statement.setInt(3,studentId);
            statement.setObject(4,date);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int courseId, int studentId, LocalDate date) {
        String sql = "DELETE FROM attendance WHERE course_id = ? AND student_id = ? AND date = ?";

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
    public void update(boolean isPresent, int courseId, int studentId, LocalDate date) {
        String sql = "UPDATE attendance SET is_present = ? WHERE course_id = ? AND student_id = ? AND date = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1,isPresent);
            statement.setInt(2,courseId);
            statement.setInt(3,studentId);
            statement.setObject(4,date);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Attendance findAttendance(int courseId, int studentId, LocalDate date) {
        String sql = "SELECT attendance.is_present, attendance.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM attendance\n" +
                "JOIN students ON attendance.student_id = students.student_id\n" +
                "JOIN courses  ON attendance.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE attendance.course_id = ? AND attendance.student_id = ? AND attendance.date = ?";

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
                Attendance attendance = new Attendance(course, student, resultSet.getObject("date", LocalDate.class), resultSet.getBoolean("is_present"));
                return attendance;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Attendance> findStudentsAttendance(int courseId, int studentId) {
        String sql = "SELECT attendance.is_present, attendance.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM attendance\n" +
                "JOIN students ON attendance.student_id = students.student_id\n" +
                "JOIN courses  ON attendance.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE attendance.course_id = ? AND attendance.student_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1,courseId);
            statement.setInt(2,studentId);
            ResultSet resultSet = statement.executeQuery();
            List<Attendance> attendanceList = new ArrayList<>();

            while(resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
                Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                Attendance attendance = new Attendance(course, student, resultSet.getObject("date", LocalDate.class), resultSet.getBoolean("is_present"));
                attendanceList.add(attendance);
            }

            return attendanceList;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Attendance> findCourseAttendance(int courseId, LocalDate date) {
        String sql = "SELECT attendance.is_present,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM enrollments\n" +
                "JOIN students ON enrollments.student_id = students.student_id\n" +
                "JOIN courses ON enrollments.course_id = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "LEFT JOIN attendance ON attendance.student_id = enrollments.student_id\n" +
                "AND attendance.course_id  = enrollments.course_id\n" +
                "AND attendance.date = ?\n" +
                "WHERE enrollments.course_id = ?\n" +
                "ORDER BY students.name";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, date);
            statement.setInt(2,courseId);
            ResultSet resultSet = statement.executeQuery();
            List<Attendance> attendanceList = new ArrayList<>();

            while(resultSet.next()) {
                Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
                Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
                Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                Attendance attendance = new Attendance(course, student, date, resultSet.getBoolean("is_present"));
                attendanceList.add(attendance);
            }

            return attendanceList;
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
