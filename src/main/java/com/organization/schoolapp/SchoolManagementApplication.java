package com.organization.schoolapp;

import com.organization.schoolapp.config.DatabaseConnection;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Grade;
import com.organization.schoolapp.repository.jdbc.*;
import com.organization.schoolapp.service.GradeService;

import java.time.LocalDate;

public class SchoolManagementApplication {
    static void main() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        JdbcStudentRepository studentRepo = new JdbcStudentRepository(dbConnection);
        JdbcTeacherRepository teacherRepo = new JdbcTeacherRepository(dbConnection);
        JdbcCourseRepository courseRepo = new JdbcCourseRepository(dbConnection);
        JdbcEnrollmentRepository enrollmentRepo = new JdbcEnrollmentRepository(dbConnection);
        JdbcGradeRepository gradeRepo = new JdbcGradeRepository(dbConnection);
        JdbcAttendanceRepository attendanceRepo = new JdbcAttendanceRepository(dbConnection);
        GradeService gradeService = new GradeService(studentRepo, courseRepo, enrollmentRepo, gradeRepo);

        // nothing here yet
    }
}
