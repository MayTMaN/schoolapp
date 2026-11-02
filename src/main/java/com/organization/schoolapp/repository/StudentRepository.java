package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    void save(Student student);

    void delete(int studentId);

    Optional<Student> findById(int studentId);

    List<Student> findAll();

}
