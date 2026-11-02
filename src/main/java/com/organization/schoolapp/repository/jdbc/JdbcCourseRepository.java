package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.db.DatabaseConnection;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.CourseRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCourseRepository implements CourseRepository {

    private final JdbcClient jdbcClient;
    private static final RowMapper<Course> COURSE_MAPPER = ((resultSet, rowNum) -> {
        Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("name"));
        Course course = new Course(resultSet.getInt("course_id"), resultSet.getString("course_name"), teacher);
        return course;
    });

    public JdbcCourseRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public void save(Course course) {
        jdbcClient.sql("INSERT INTO courses (course_name, teacher_id) VALUES (?, ?)")
                .param(1, course.getCourseName())
                .param(2, course.getTeacher().getId())
                .update();
    }

    @Override
    public void delete(int courseId) {
        jdbcClient.sql("DELETE FROM courses WHERE course_id = ?")
                .param(1, courseId)
                .update();
    }

    @Override
    public void update(Course course) {
        jdbcClient.sql("UPDATE courses SET course_name = ?, teacher_id = ? WHERE course_id = ?")
                .param(1, course.getCourseName())
                .param(2, course.getTeacher().getId())
                .param(3, course.getCourseId())
                .update();
    }

    @Override
    public Optional<Course> findById(int courseId) {
        return jdbcClient.sql("SELECT courses.course_id, courses.course_name, teachers.teacher_id, teachers.name\n" +
                "FROM courses\n" +
                "JOIN teachers ON courses.teacher_id = teachers.teacher_id\n" +
                "WHERE courses.course_id = ?")
                .param(1, courseId)
                .query(COURSE_MAPPER)
                .optional();
    }

    @Override
    public List<Course> findAll() {
        return jdbcClient.sql("SELECT * FROM courses JOIN teachers ON courses.teacher_id = teachers.teacher_id")
                .query(COURSE_MAPPER)
                .list();
    }
}
