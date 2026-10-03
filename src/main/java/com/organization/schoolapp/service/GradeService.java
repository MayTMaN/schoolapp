package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Grade;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.EnrollmentRepository;
import com.organization.schoolapp.repository.GradeRepository;
import com.organization.schoolapp.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

public class GradeService {
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepo;

    public GradeService (StudentRepository studentRepo, CourseRepository courseRepo, EnrollmentRepository enrollmentRepository, GradeRepository gradeRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepository = enrollmentRepository;
        this.gradeRepo = gradeRepo;
    }

    public void gradeStudent (double grade, int courseId, int studentId, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date not provided");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date can't be in the future");
        }

        if (grade < 0 || grade > 10) {
            throw new IllegalArgumentException("Grade has to be between 0 and 10");
        }

        Student student = studentRepo.findById(studentId);
        Course course = courseRepo.findById(courseId);

        if (student == null || course == null) {
            throw new IllegalArgumentException("Incorrect student or course");
        }

        if (!enrollmentRepository.isEnrolled(course.getCourseId(), student.getId())) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        if (gradeRepo.findGrade(courseId, studentId, date) != null) {
            throw new IllegalArgumentException("A grade on this date already exists");
        }

        gradeRepo.setGrade(grade, courseId, studentId, date);
    }

    public double getAverage(int courseId, int studentId) {
        Student student = studentRepo.findById(studentId);
        Course course = courseRepo.findById(courseId);

        if (student == null || course == null) {
            throw new IllegalArgumentException("Incorrect student or course");
        }

        if (!enrollmentRepository.isEnrolled(courseId,studentId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        List<Grade> grades = gradeRepo.findStudentGrades(courseId, studentId);

        if (grades.isEmpty()) {
            throw new IllegalStateException("This student doesn't have any grades");
        }

        double sum = 0;
        for (Grade g : grades) {
            sum += g.getGrade();
        }

        double average = sum / grades.size();
        return average;
    }

}
