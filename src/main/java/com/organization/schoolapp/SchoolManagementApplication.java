package com.organization.schoolapp;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.JdbcCourseRepository;
import com.organization.schoolapp.repository.JdbcStudentRepository;
import com.organization.schoolapp.repository.JdbcTeacherRepository;

public class SchoolManagementApplication {
    static void main() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        JdbcStudentRepository studentRepo = new JdbcStudentRepository(dbConnection);
        JdbcTeacherRepository teacherRepo = new JdbcTeacherRepository(dbConnection);
        JdbcCourseRepository courseRepo = new JdbcCourseRepository(dbConnection);

        for (Course c : courseRepo.findAll()) {
            System.out.println(" " + c.getCourseId() + " " + c.getCourseName() + " " + c.getTeacher().getId() + " " + c.getTeacher().getName());
        }
    }
}
