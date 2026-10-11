package com.organization.schoolapp;

import com.organization.schoolapp.db.DatabaseConnection;
import com.organization.schoolapp.repository.jdbc.*;
import com.organization.schoolapp.service.*;
import com.organization.schoolapp.ui.CourseMenu;
import com.organization.schoolapp.ui.MainMenu;
import com.organization.schoolapp.ui.StudentMenu;
import com.organization.schoolapp.ui.TeacherMenu;

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
        AttendanceService attendanceService = new AttendanceService(studentRepo, courseRepo, attendanceRepo, enrollmentRepo);
        StudentService studentService = new StudentService(studentRepo);
        TeacherService teacherService = new TeacherService(teacherRepo, courseRepo);
        CourseService courseService = new CourseService(courseRepo, teacherRepo);
        EnrollmentService enrollmentService = new EnrollmentService(studentRepo, courseRepo, enrollmentRepo);
        MainMenu mainMenu = new MainMenu(new StudentMenu(studentService, gradeService, attendanceService),new TeacherMenu(teacherService, gradeService, attendanceService), new CourseMenu(courseService, enrollmentService, attendanceService));

        mainMenu.start();
    }
}
