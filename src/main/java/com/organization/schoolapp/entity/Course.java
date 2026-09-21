package com.organization.schoolapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private Teacher teacher;
    private List<Student> students = new ArrayList<>();
    private int courseId;
    private String courseName;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int newCourseId) {
        this.courseId = newCourseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String newCourseName) {
        this.courseName = newCourseName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void enroll(Student student) {
        students.add(student);
    }

    public void dismiss(Student student) {
        students.remove(student);
    }
}
