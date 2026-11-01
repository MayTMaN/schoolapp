package com.organization.schoolapp;

import com.organization.schoolapp.db.DatabaseConnection;
import com.organization.schoolapp.repository.jdbc.*;
import com.organization.schoolapp.service.*;
import com.organization.schoolapp.ui.CourseMenu;
import com.organization.schoolapp.ui.MainMenu;
import com.organization.schoolapp.ui.StudentMenu;
import com.organization.schoolapp.ui.TeacherMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolManagementApplication {
    static void main(String[] args) {
        SpringApplication.run(SchoolManagementApplication.class, args);
    }
}
