package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.entity.Attendance;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.AttendanceRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAttendanceRepository implements AttendanceRepository {
    private final JdbcClient jdbcClient;
    private final static RowMapper<Attendance> ATTENDANCE_MAPPER = (resultSet, rowNum) -> {
        Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
        Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
        Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
        Attendance attendance = new Attendance(course, student, resultSet.getObject("date", LocalDate.class), resultSet.getBoolean("is_present"));
        return attendance;
    };

    public JdbcAttendanceRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(boolean isPresent, int courseId, int studentId, LocalDate date) {
        jdbcClient.sql("INSERT INTO attendance (is_present, course_id, student_id, date) VALUES (?,?,?,?)")
                .param(1, isPresent)
                .param(2, courseId)
                .param(3,studentId)
                .param(4, date)
                .update();

    }

    @Override
    public void delete(int courseId, int studentId, LocalDate date) {
        jdbcClient.sql("DELETE FROM attendance WHERE course_id = ? AND student_id = ? AND date = ?")
                .param(1, courseId)
                .param(2,studentId)
                .param(3, date)
                .update();
    }

    @Override
    public void update(boolean isPresent, int courseId, int studentId, LocalDate date) {
        jdbcClient.sql("UPDATE attendance SET is_present = ? WHERE course_id = ? AND student_id = ? AND date = ?")
                .param(1, isPresent)
                .param(2,courseId)
                .param(3, studentId)
                .param(4, date)
                .update();
    }

    @Override
    public Optional<Attendance> findAttendance(int courseId, int studentId, LocalDate date) {
        return jdbcClient.sql("SELECT attendance.is_present, attendance.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM attendance\n" +
                "JOIN students ON attendance.student_id = students.student_id\n" +
                "JOIN courses  ON attendance.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE attendance.course_id = ? AND attendance.student_id = ? AND attendance.date = ?")
                .param(1, courseId).param(2, studentId).param(3, date)
                .query(ATTENDANCE_MAPPER)
                .optional();
    }

    @Override
    public List<Attendance> findStudentsAttendance(int courseId, int studentId) {
        return jdbcClient.sql("SELECT attendance.is_present, attendance.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM attendance\n" +
                "JOIN students ON attendance.student_id = students.student_id\n" +
                "JOIN courses  ON attendance.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE attendance.course_id = ? AND attendance.student_id = ?")
                .param(1, courseId).param(2, studentId)
                .query(ATTENDANCE_MAPPER).list();
    }

    @Override
    public List<Attendance> findCourseAttendance(int courseId, LocalDate date) {
        return jdbcClient.sql("SELECT attendance.is_present,\n" +
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
                "ORDER BY students.name")
                .param(1, date).param(2, courseId)
                .query((resultSet, rowNum) -> {
                    Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
                    Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
                    Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
                    Attendance attendance = new Attendance(course, student, date, resultSet.getBoolean("is_present"));
                    return attendance;
                }).list();
    }
}
