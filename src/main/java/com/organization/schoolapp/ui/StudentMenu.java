package com.organization.schoolapp.ui;

import com.organization.schoolapp.entity.Attendance;
import com.organization.schoolapp.entity.Grade;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.service.AttendanceService;
import com.organization.schoolapp.service.GradeService;
import com.organization.schoolapp.service.StudentService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.organization.schoolapp.ui.MainMenu.scanner;

public class StudentMenu {
    private final StudentService studentService;
    private final GradeService gradeService;
    private final AttendanceService attendanceService;

    public StudentMenu(StudentService studentService, GradeService gradeService, AttendanceService attendanceService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.attendanceService = attendanceService;
    }

    public void start() {
        printStudentMenu();
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> handleAddStudent();
            case 2 -> handleDeleteStudent();
            case 3 -> handleGetStudent();
            case 4 -> handleGetAllStudents();
            case 5 -> handleGetGrade();
            case 6 -> handleGetStudentGrades();
            case 7 -> handleGetAverageGrade();
            case 8 -> handleGetAttendance();
            case 9 -> handleGetStudentAttendance();
            case 10 -> handleGetAttendancePercentage();
            default -> System.out.println("Invalid option");
        }
    }

    public void printStudentMenu() {

        System.out.println(" ");
        System.out.println("||| Student Menu |||");
        System.out.println("1. Add a student");
        System.out.println("2. Delete a student");
        System.out.println("3. Find a Student by ID");
        System.out.println("4. List all students");
        System.out.println("5. Find a Grade by date");
        System.out.println("6. List student's grades");
        System.out.println("7. Get Student's GPA");
        System.out.println("8. Find Attendance by date");
        System.out.println("9. List student's attendance");
        System.out.println("10. Get Student's attendance percentage");
        System.out.println("Choose your option: ");

    }

    private void handleAddStudent() {
        System.out.println("Enter student's first name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        try {
            studentService.addStudent(name);
            System.out.println("Student added!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteStudent() {
        System.out.println("Enter student ID: ");
        int studentId = scanner.nextInt();
        try {
            studentService.deleteStudent(studentId);
            System.out.println("Student deleted!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetStudent() {
        System.out.println("Enter student ID: ");
        int studentId = scanner.nextInt();
        try {
            Student student = studentService.getStudent(studentId);
            System.out.println("Name: " + student.getName());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetAllStudents() {
        List<Student> studentList = studentService.getAllStudents();
        if (studentList.isEmpty()) {
            System.out.println("No students yet");
        } else {
            System.out.println("|| Student List ||");
            for(Student student : studentList) {
                System.out.println("ID: " + student.getId() + " | Name: " + student.getName());
            }
        }


    }

    private void handleGetGrade() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        System.out.println("Enter date (yyyy-mm-dd): ");
        scanner.nextLine();
        try {
            LocalDate date = LocalDate.parse(scanner.nextLine());
            Grade grade = gradeService.getGrade(courseId, studentId, date);
            System.out.println("Grade: " + grade.getGrade());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetStudentGrades() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();

        try {
            List<Grade> grades = gradeService.getStudentGrades(courseId, studentId);
            if(grades.isEmpty()) {
                System.out.println("No grades yet");
            } else {
                System.out.println("|| Grades ||");
                for (Grade grade : grades) {
                    System.out.println("Grade: " + grade.getGrade() + " | Date: " + grade.getDate());
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetAverageGrade() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();

        try {
            double average = gradeService.getAverage(courseId, studentId);
            System.out.println("Student's GPA: " + average); // implement formatting for decimals
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetAttendance() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter date (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            Attendance attendance = attendanceService.getAttendance(courseId, studentId, date);
            System.out.println(attendance.isPresent() ? "Present" : "Absent");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetStudentAttendance() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();

        try {
            List<Attendance> attendanceList = attendanceService.getStudentAttendance(courseId, studentId);
            if (attendanceList.isEmpty()) {
                System.out.println("No attendance recorded yet");
            } else {
                System.out.println("|| Attendance ||");
                for (Attendance attendance : attendanceList) {
                    String status = attendance.isPresent() ? "present" : "absent";
                    System.out.println("Date: " + attendance.getDate() + " | " + status);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetAttendancePercentage() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();

        try {
            double percentage = attendanceService.getAttendancePercentage(courseId, studentId);
            System.out.println("Attendance: " + String.format("%.1f%%", percentage));
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
