package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.EnrollmentRepository;
import com.organization.schoolapp.repository.StudentRepository;

import java.util.List;

public class EnrollmentService {

    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;

    public EnrollmentService(StudentRepository studentRepo, CourseRepository courseRepo, EnrollmentRepository enrollmentRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public void enrollStudent(int courseId, int studentId) {
        checkStudentAndCourseExist(courseId, studentId);
        if (enrollmentRepo.isEnrolled(courseId, studentId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        enrollmentRepo.enroll(courseId, studentId);
    }

    public void dismissStudent(int courseId, int studentId) {
        checkStudentAndCourseExist(courseId, studentId);
        checkEnrolled(courseId, studentId);
        enrollmentRepo.dismiss(courseId, studentId);
    }

    public List<Student> getStudentsByCourse (int courseId) {
        Course course = courseRepo.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course " + courseId + " doesn't exist");
        }

        List<Student> enrolledStudents = enrollmentRepo.findStudentsByCourse(courseId);

        return enrolledStudents;
    }

    private void checkStudentAndCourseExist (int courseId, int studentId) {
        Student student = studentRepo.findById(studentId);
        Course course = courseRepo.findById(courseId);

        if (student == null || course == null) {
            throw new IllegalArgumentException("Incorrect student or course");
        }
    }

    private void checkEnrolled (int courseId, int studentId) {
        if (!enrollmentRepo.isEnrolled(courseId, studentId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
    }
}
