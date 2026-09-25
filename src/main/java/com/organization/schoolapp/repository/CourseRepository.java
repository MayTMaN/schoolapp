package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Course;

import java.util.List;

public interface CourseRepository {

    void save (Course course);

    void delete (int courseId);

    void update (Course course);

    Course findById(int courseId);

    List<Course> findAll();

}
