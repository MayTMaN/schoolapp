package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Grade;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.EnrollmentRepository;
import com.organization.schoolapp.repository.GradeRepository;
import com.organization.schoolapp.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GradeService {
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final GradeRepository gradeRepo;

    public GradeService (StudentRepository studentRepo, CourseRepository courseRepo, EnrollmentRepository enrollmentRepository, GradeRepository gradeRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepository;
        this.gradeRepo = gradeRepo;
    }

    public void gradeStudent (double grade, int courseId, int studentId, LocalDate date) {
        checkDate(date);
        checkGrade(grade);
        checkStudentAndCourseExist(courseId, studentId);
        checkEnrolled(courseId, studentId);

        if (gradeRepo.findGrade(courseId, studentId, date).isPresent()) {
            throw new IllegalArgumentException("A grade on this date already exists");
        }

        gradeRepo.setGrade(grade, courseId, studentId, date);
    }

    public void changeGrade (double grade, int courseId, int studentId, LocalDate date) {
        checkDate(date);
        checkGrade(grade);
        checkStudentAndCourseExist(courseId, studentId);

        if (gradeRepo.findGrade(courseId, studentId, date).isEmpty()) {
            throw new IllegalStateException("This grade does not exist");
        }

        gradeRepo.updateGrade(courseId, studentId, date, grade);
    }

    public void removeGrade (int courseId, int studentId, LocalDate date) {
        checkDate(date);
        checkStudentAndCourseExist(courseId, studentId);

        if (gradeRepo.findGrade(courseId, studentId, date).isEmpty()) {
            throw new IllegalStateException("This grade does not exist");
        }

        gradeRepo.removeGrade(courseId, studentId, date);
    }

    public double getAverage(int courseId, int studentId) {
        checkStudentAndCourseExist(courseId, studentId);
        checkEnrolled(courseId, studentId);

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

    public Grade getGrade(int courseId, int studentId, LocalDate date) {
        checkDate(date);
        checkStudentAndCourseExist(courseId, studentId);

        return gradeRepo.findGrade(courseId, studentId, date)
                .orElseThrow(() -> new IllegalStateException("This grade does not exist"));
    }

    public List<Grade> getStudentGrades(int courseId, int studentId) {
        checkStudentAndCourseExist(courseId, studentId);

        return gradeRepo.findStudentGrades(courseId, studentId);
    }

    public List<Grade> getCourseGrades(int courseId) {
        checkCourseExists(courseId);

        return gradeRepo.findGradesByCourse(courseId);
    }

    private void checkDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date not provided");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date can't be in the future");
        }
    }

    private void checkGrade (double grade) {
        if (grade < 0 || grade > 10) {
            throw new IllegalArgumentException("Grade has to be between 0 and 10");
        }
    }

    private void checkStudentAndCourseExist (int courseId, int studentId) {
        if (studentRepo.findById(studentId).isEmpty() || courseRepo.findById(courseId).isEmpty()) {
            throw new IllegalArgumentException("Incorrect student or course");
        }
    }

    private void checkCourseExists(int courseId) {
        courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("No course with id " + courseId));
    }

    private void checkEnrolled (int courseId, int studentId) {
        if (!enrollmentRepo.isEnrolled(courseId, studentId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
    }

}
