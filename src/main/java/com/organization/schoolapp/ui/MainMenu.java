package com.organization.schoolapp.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MainMenu {

    private final StudentMenu studentMenu;
    private final TeacherMenu teacherMenu;
    private final CourseMenu courseMenu;
    private final Scanner scanner;

    public MainMenu(StudentMenu studentMenu, TeacherMenu teacherMenu, CourseMenu courseMenu, Scanner scanner) {
        this.studentMenu = studentMenu;
        this.teacherMenu = teacherMenu;
        this.courseMenu = courseMenu;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> studentMenu.start();
                case 2 -> teacherMenu.start();
                case 3 -> courseMenu.start();
                case 0 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void printMenu() {

        System.out.println(" ");
        System.out.println("||| MENU |||");
        System.out.println("1. Students");
        System.out.println("2. Teachers");
        System.out.println("3. Courses");
        System.out.println("0. Exit");
        System.out.println("Choose your option: ");

    }
}
