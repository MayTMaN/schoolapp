package com.organization.schoolapp.ui;

import com.organization.schoolapp.entity.Attendance;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.service.AttendanceService;
import com.organization.schoolapp.service.CourseService;
import com.organization.schoolapp.service.EnrollmentService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.organization.schoolapp.ui.MainMenu.scanner;

public class CourseMenu {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final AttendanceService attendanceService;

    public CourseMenu(CourseService courseService, EnrollmentService enrollmentService, AttendanceService attendanceService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.attendanceService = attendanceService;
    }

    public void start() {
        printCourseMenu();
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> handleAddCourse();
            case 2 -> handleDeleteCourse();
            case 3 -> handleGetCourse();
            case 4 -> handleGetAllCourses();
            case 5 -> handleRenameCourse();
            case 6 -> handleChangeTeacher();
            case 7 -> handleEnrollStudent();
            case 8 -> handleDismissStudent();
            case 9 -> handleGetStudentsInCourse();
            case 10 -> handleGetClassAttendance();
            default -> System.out.println("Invalid option");
        }
    }

    public void printCourseMenu() {
        System.out.println(" ");
        System.out.println("||| Course Menu |||");
        System.out.println("1. Add a course");
        System.out.println("2. Delete a course");
        System.out.println("3. Find a course by ID");
        System.out.println("4. List all courses");
        System.out.println("5. Rename Course");
        System.out.println("6. Change the teacher of the course");
        System.out.println("7. Enroll a student in a course");
        System.out.println("8. Dismiss a student from a course");
        System.out.println("9. List all students in a course");
        System.out.println("10. List attendance of students in a course");
        System.out.println("Choose your option: ");
    }

    private void handleAddCourse() {
        System.out.println("Enter course name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.println("Enter teacher ID: ");
        int teacherId = scanner.nextInt();
        try {
            courseService.addCourse(name, teacherId);
            System.out.println("Course added!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteCourse() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        try {
            courseService.deleteCourse(courseId);
            System.out.println("Course deleted!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetCourse() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        try {
            Course course = courseService.getCourse(courseId);
            System.out.println("Name: " + course.getCourseName() + " | Teacher: " + course.getTeacher().getName());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetAllCourses() {
        List<Course> courseList = courseService.getAllCourses();

        if (courseList.isEmpty()) {
            System.out.println("No courses yet");
        } else {
            System.out.println("|| Course List ||");
            for (Course course : courseList) {
                System.out.println("ID: " + course.getCourseId() + " | Name: " + course.getCourseName() + " | Teacher: " + course.getTeacher().getName());
            }
        }
    }

    private void handleRenameCourse() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter new course name: ");
        String newName = scanner.nextLine();
        try {
            courseService.renameCourse(courseId, newName);
            System.out.println("Course renamed!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangeTeacher() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter new teacher ID: ");
        int teacherId = scanner.nextInt();
        try {
            courseService.changeTeacher(courseId, teacherId);
            System.out.println("Teacher changed!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleEnrollStudent() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter student ID: ");
        int studentId = scanner.nextInt();
        try {
            enrollmentService.enrollStudent(courseId, studentId);
            System.out.println("Student enrolled!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDismissStudent() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        System.out.println("Enter student ID: ");
        int studentId = scanner.nextInt();
        try {
            enrollmentService.dismissStudent(courseId, studentId);
            System.out.println("Student dismissed!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetStudentsInCourse() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        try {
            List<Student> students = enrollmentService.getStudentsByCourse(courseId);
            if (students.isEmpty()) {
                System.out.println("No students enrolled yet");
            } else {
                System.out.println("|| Students in course ||");
                for (Student student : students) {
                    System.out.println("ID: " + student.getId() + " | Name: " + student.getName());
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleGetClassAttendance() {
        System.out.println("Enter course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.println("Enter date (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            List<Attendance> classAttendance = attendanceService.getClassAttendance(courseId, date);
            if (classAttendance.isEmpty()) {
                System.out.println("No students enrolled yet");
            } else {
                System.out.println("|| Attendance on " + date + " ||");
                for (Attendance attendance : classAttendance) {
                    Student student = attendance.getStudent();
                    String status = attendance.isPresent() ? "present" : "absent";
                    System.out.println("ID: " + student.getId() + " | Name: " + student.getName() + " | " + status);
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use the format yyyy-mm-dd");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
