package com.organization.schoolapp.entity;

import java.time.LocalDate;

public class Attendance {
    private Course course;
    private Student student;
    private LocalDate date;
    private boolean isPresent;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        this.isPresent = present;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
