package com.organization.schoolapp.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final MainMenu mainMenu;

    public ConsoleRunner (MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public void run(String... args) throws Exception {
        mainMenu.start();
    }
}
