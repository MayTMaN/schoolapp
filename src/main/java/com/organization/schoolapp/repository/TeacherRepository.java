package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Teacher;

import java.util.List;

public interface TeacherRepository {

    void save(Teacher teacher);

    void delete(int teacherId);

    Teacher findById(int teacherId);

    List<Teacher> findAll();

}
