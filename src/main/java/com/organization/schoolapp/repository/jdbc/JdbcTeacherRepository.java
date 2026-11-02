package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.db.DatabaseConnection;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.TeacherRepository;
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
public class JdbcTeacherRepository implements TeacherRepository {
    private final JdbcClient jdbcClient;
    private final static RowMapper<Teacher> TEACHER_MAPPER = (resultSet, rowNum) -> {
        Teacher teacher = new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("name"));
        return teacher;
    };

    public JdbcTeacherRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Teacher teacher) {
        jdbcClient.sql("INSERT INTO teachers (name) VALUES (?)")
                .param(1, teacher.getName())
                .update();
    }

    @Override
    public void delete(int teacherId) {
        jdbcClient.sql("DELETE FROM teachers WHERE teacher_id = ?")
                .param(1, teacherId)
                .update();
    }

    @Override
    public Optional<Teacher> findById(int teacherId) {
        return jdbcClient.sql("SELECT * FROM teachers WHERE teacher_id = ?")
                .param(1, teacherId)
                .query(TEACHER_MAPPER)
                .optional();
    }

    @Override
    public List<Teacher> findAll() {
        return jdbcClient.sql("SELECT * FROM teachers")
                .query(TEACHER_MAPPER)
                .list();
    }
}
