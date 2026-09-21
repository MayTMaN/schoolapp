package com.organization.schoolapp.entity;

import java.time.LocalDate;

public class Grade {
    private Student student;
    private Course course;
    private double grade;
    private LocalDate date;

    public double getGrade() {
        return grade;
    }

    public void setGrade(double newGrade) {
        this.grade = newGrade;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
