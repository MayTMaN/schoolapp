package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.db.DatabaseConnection;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Grade;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.GradeRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcGradeRepository implements GradeRepository {
    private final JdbcClient jdbcClient;
    private final static RowMapper<Grade> GRADE_MAPPER = ((resultSet, rowNum) -> {
        Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("student_name"));
        Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("teacher_name"));
        Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
        Grade grade = new Grade(student,course,resultSet.getDouble("grade"), resultSet.getObject("date", LocalDate.class));
        return grade;
    });

    public JdbcGradeRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void setGrade(double grade, int courseId, int studentId, LocalDate date) {
        jdbcClient.sql("INSERT INTO grades (grade, course_id, student_id, date) VALUES (?,?,?,?)")
                .param(1, grade)
                .param(2, courseId)
                .param(3, studentId)
                .param(4, date)
                .update();
    }

    @Override
    public void updateGrade(int courseId, int studentId, LocalDate date, double newGrade) {
        jdbcClient.sql("UPDATE grades SET grade = ? WHERE course_id = ? AND student_id = ? AND date = ?")
                .param(1, newGrade)
                .param(2, courseId)
                .param(3, studentId)
                .param(4, date)
                .update();
    }

    @Override
    public void removeGrade(int courseId, int studentId, LocalDate date) {
        jdbcClient.sql("DELETE FROM grades WHERE course_id = ? AND student_id = ? AND date = ?")
                .param(1, courseId)
                .param(2, studentId)
                .param(3, date)
                .update();
    }

    @Override
    public Optional<Grade> findGrade(int courseId, int studentId, LocalDate date) {
        return jdbcClient.sql("SELECT grades.grade, grades.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM grades\n" +
                "JOIN students ON grades.student_id = students.student_id\n" +
                "JOIN courses  ON grades.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE grades.course_id = ? AND grades.student_id = ? AND grades.date = ?")
                .param(1, courseId)
                .param(2, studentId)
                .param(3, date)
                .query(GRADE_MAPPER).optional();
    }

    @Override
    public List<Grade> findStudentGrades(int courseId, int studentId) {
        return jdbcClient.sql("SELECT grades.grade, grades.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM grades\n" +
                "JOIN students ON grades.student_id = students.student_id\n" +
                "JOIN courses  ON grades.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE grades.course_id = ? AND grades.student_id = ?")
                .param(1, courseId)
                .param(2, studentId)
                .query(GRADE_MAPPER).list();
    }

    @Override
    public List<Grade> findGradesByCourse(int courseId) {
        return jdbcClient.sql("SELECT grades.grade, grades.date,\n" +
                "students.student_id, students.name AS student_name,\n" +
                "courses.course_id, courses.course_name,\n" +
                "teachers.teacher_id, teachers.name AS teacher_name\n" +
                "FROM grades\n" +
                "JOIN students ON grades.student_id = students.student_id\n" +
                "JOIN courses  ON grades.course_id  = courses.course_id\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE grades.course_id = ?")
                .param(1, courseId)
                .query(GRADE_MAPPER).list();
    }
}
