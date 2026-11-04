package com.organization.schoolapp.repository.jdbc;

import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.StudentRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStudentRepository implements StudentRepository {
    private final JdbcClient jdbcClient;
    private final static RowMapper<Student> STUDENT_MAPPER = (resultSet, rowNum) -> {
        Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("name"));
        return student;
    };

    public JdbcStudentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Student student) {
        jdbcClient.sql("INSERT INTO students (name) VALUES (?)")
                .param(1, student.getName())
                .update();
    }

    @Override
    public void delete(int studentId) {
        jdbcClient.sql("DELETE FROM students WHERE student_id = ?")
                .param(1, studentId)
                .update();
    }

    @Override
    public Optional<Student> findById(int studentId) {
        return jdbcClient.sql("SELECT * FROM students WHERE student_id = ?")
                .param(1, studentId)
                .query(STUDENT_MAPPER)
                .optional();
    }

    @Override
    public List<Student> findAll() {
        return jdbcClient.sql("SELECT * FROM students")
                .query(STUDENT_MAPPER)
                .list();
    }

}
