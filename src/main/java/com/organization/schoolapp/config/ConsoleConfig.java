package com.organization.schoolapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class ConsoleConfig {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

}
