package com.organization.schoolapp;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.service.StudentService;
import com.organization.schoolapp.service.TeacherService;

public class SchoolManagementApplication {
    static void main() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        StudentService studentRepo = new StudentService(dbConnection);
        TeacherService teacherRepo = new TeacherService(dbConnection);

        studentRepo.findById(2);

        for (Student s : studentRepo.findAll()) {
            System.out.println("Student ID: " + s.getId() + " | Student Name: " + s.getName());
        }

        for (Student s : studentRepo.findAll()) {
            System.out.println("Student ID: " + s.getId() + " | Student Name: " + s.getName());
        }
    }
}
