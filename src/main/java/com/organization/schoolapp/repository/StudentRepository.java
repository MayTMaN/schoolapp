package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Student;

import java.util.List;

public interface StudentRepository {

    void save(Student student);

    void delete(int studentId);

    Student findById(int studentId);

    List<Student> findAll();

}
