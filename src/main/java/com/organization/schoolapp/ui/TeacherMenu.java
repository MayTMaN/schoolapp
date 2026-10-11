package com.organization.schoolapp.ui;

import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.service.AttendanceService;
import com.organization.schoolapp.service.GradeService;
import com.organization.schoolapp.service.TeacherService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.organization.schoolapp.ui.MainMenu.scanner;

public class TeacherMenu {
    private final TeacherService teacherService;
    private final GradeService gradeService;
    private final AttendanceService attendanceService;

    public TeacherMenu(TeacherService teacherService, GradeService gradeService, AttendanceService attendanceService) {
        this.teacherService = teacherService;
        this.gradeService = gradeService;
        this.attendanceService = attendanceService;
    }

    public void start() {
        printTeacherMenu();
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> handleAddTeacher();
            case 2 -> handleDeleteTeacher();
            case 3 -> handleGetTeacher();
            case 4 -> handleGetAllTeachers();
            case 5 -> handleGradeStudent();
            case 6 -> handleChangeGrade();
            case 7 -> handleRemoveGrade();
            case 8 -> handleMarkAttendance();
            case 9 -> handleChangeAttendance();
            case 10 -> handleRemoveAttendance();
            default -> System.out.println("Invalid option");
        }
    }

    public void printTeacherMenu() {
        System.out.println(" ");
        System.out.println("||| Teacher Menu |||");
        System.out.println("1. Add a teacher");
        System.out.println("2. Delete a teacher");
        System.out.println("3. Find a teacher by ID");
        System.out.println("4. List all teachers");
        System.out.println("5. Grade a student");
        System.out.println("6. Change a grade");
        System.out.println("7. Remove a grade");
        System.out.println("8. Mark Attendance");
        System.out.println("9. Change Attendance");
        System.out.println("10. Remove attendance");
        System.out.println("Choose your option: ");
    }

    private void handleAddTeacher() {
        System.out.println("Enter teacher's name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        try {
            teacherService.addTeacher(name);
            System.out.println("Teacher added!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteTeacher() {
        System.out.println("Enter teacher ID: ");
        int teacherId = scanner.nextInt();
        try {
            teacherService.deleteTeacher(teacherId);
            System.out.println("Teacher deleted!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetTeacher() {
        System.out.println("Enter teacher ID: ");
        int teacherId = scanner.nextInt();
        try {
            Teacher teacher = teacherService.getTeacher(teacherId);
            System.out.println("Name: " + teacher.getName());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetAllTeachers() {
        List<Teacher> teacherList = teacherService.getAllTeachers();

        if (teacherList.isEmpty()) {
            System.out.println("No teachers yet");
        } else {
            System.out.println("|| Teacher List ||");
            for (Teacher teacher : teacherList) {
                System.out.println("ID: " + teacher.getId() + " | Name: " + teacher.getName());
            }
        }
    }

    private void handleGradeStudent() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter grade (0-10): ");
            double grade = Double.parseDouble(scanner.nextLine());
            System.out.println("Enter date (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            gradeService.gradeStudent(grade, courseId, studentId, date);
            System.out.println("Grade saved!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade. Enter a number like 8 or 8.5");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangeGrade() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter the date of the grade (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            System.out.println("Enter new grade (0-10): ");
            double newGrade = Double.parseDouble(scanner.nextLine());

            gradeService.changeGrade(newGrade, courseId, studentId, date);
            System.out.println("Grade changed!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade. Enter a number like 8 or 8.5");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRemoveGrade() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter the date of the grade (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            gradeService.removeGrade(courseId, studentId, date);
            System.out.println("Grade removed!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleMarkAttendance() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter date (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            System.out.println("Was the student present? (y/n): ");
            boolean isPresent = parsePresence(scanner.nextLine());

            attendanceService.markAttendance(isPresent, courseId, studentId, date);
            System.out.println("Attendance marked!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangeAttendance() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter the date of the attendance (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            System.out.println("Was the student present? (y/n): ");
            boolean isPresent = parsePresence(scanner.nextLine());

            attendanceService.changeAttendance(isPresent, courseId, studentId, date);
            System.out.println("Attendance changed!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRemoveAttendance() {
        System.out.println("Enter Course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter the date of the attendance (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            attendanceService.removeAttendance(courseId, studentId, date);
            System.out.println("Attendance removed!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean parsePresence(String answer) {
        if (answer.equalsIgnoreCase("y")) {
            return true;
        }

        if (answer.equalsIgnoreCase("n")) {
            return false;
        }

        throw new IllegalArgumentException("Please answer y or n");
    }
}
