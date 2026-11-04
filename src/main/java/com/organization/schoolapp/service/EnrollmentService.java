package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.EnrollmentRepository;
import com.organization.schoolapp.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course " + courseId + " doesn't exist"));

        List<Student> enrolledStudents = enrollmentRepo.findStudentsByCourse(courseId);

        return enrolledStudents;
    }

    private void checkStudentAndCourseExist (int courseId, int studentId) {
        if (studentRepo.findById(studentId).isEmpty() || courseRepo.findById(courseId).isEmpty()) {
            throw new IllegalArgumentException("Incorrect student or course");
        }
    }

    private void checkEnrolled (int courseId, int studentId) {
        if (!enrollmentRepo.isEnrolled(courseId, studentId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
    }
}
