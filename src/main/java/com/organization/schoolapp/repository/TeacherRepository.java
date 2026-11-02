package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository {

    void save(Teacher teacher);

    void delete(int teacherId);

    Optional<Teacher> findById(int teacherId);

    List<Teacher> findAll();

}
