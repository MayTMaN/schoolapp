package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.EnrollmentRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcEnrollmentRepository implements EnrollmentRepository {
    private final JdbcClient jdbcClient;

    public JdbcEnrollmentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void enroll(int courseId, int studentId) {
        jdbcClient.sql("INSERT INTO enrollments (course_id, student_id) VALUES (?,?)")
                .param(1, courseId)
                .param(2, studentId)
                .update();
    }

    @Override
    public void dismiss(int courseId, int studentId) {
        jdbcClient.sql("DELETE FROM enrollments WHERE course_id = ? AND student_id = ?")
                .param(1, courseId)
                .param(2, studentId)
                .update();
    }

    @Override
    public boolean isEnrolled(int courseId, int studentId) {
        return jdbcClient.sql("SELECT 1 FROM enrollments WHERE course_id = ? AND student_id = ?")
                .param(1, courseId)
                .param(2, studentId)
                .query(Integer.class).optional().isPresent();
    }

    @Override
    public List<Student> findStudentsByCourse(int courseId) {
        return jdbcClient.sql("SELECT students.student_id, students.name\n" +
                "FROM enrollments\n" +
                "JOIN students ON enrollments.student_id = students.student_id\n" +
                "WHERE enrollments.course_id = ?")
                .param(1, courseId)
                .query((resultSet, rowNum) -> {
                    Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("name"));
                    return student;
                }).list();
    }
}
