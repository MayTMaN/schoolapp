package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Grade;

import java.time.LocalDate;
import java.util.List;

public interface GradeRepository {

    void setGrade(double grade, int courseId, int studentId, LocalDate date);

    void updateGrade(int courseId, int studentId, LocalDate date, double newGrade);

    void removeGrade(int courseId, int studentId, LocalDate date);

    Grade findGrade(int courseId, int studentId, LocalDate date);

    List<Grade> findStudentGrades(int courseId, int studentId);

    List<Grade> findGradesByCourse(int courseId);
}
