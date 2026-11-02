package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    void save (Course course);

    void delete (int courseId);

    void update (Course course);

    Optional<Course> findById(int courseId);

    List<Course> findAll();

}
