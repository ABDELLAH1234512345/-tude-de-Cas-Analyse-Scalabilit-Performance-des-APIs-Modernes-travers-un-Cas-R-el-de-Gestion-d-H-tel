package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the REST Web Services Performance
 * Benchmark project.
 * <p>
 * This application serves as the base variant for comparing different REST
 * service implementations.
 * It uses Spring Boot with Spring Data JPA and Spring Data REST to provide
 * RESTful endpoints
 * for managing Items and Categories.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class App {
    /**
     * Main entry point for the Spring Boot application.
     * <p>
     * Initializes and starts the Spring application context, enabling
     * auto-configuration
     * and component scanning for the entire application.
     * </p>
     * 
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
