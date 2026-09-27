package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Student;

import java.util.List;

public interface EnrollmentRepository {

    void enroll (int courseId, int studentId);

    void dismiss (int courseId, int studentId);

    boolean isEnrolled (int courseId, int studentId);

    List<Student> findStudentsByCourse(int courseId);

}
