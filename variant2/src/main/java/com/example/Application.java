package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Variant C: Spring MVC + Hibernate.
 * <p>
 * This variant uses standard Spring Boot auto-configuration for Hibernate
 * and Spring MVC (RestControllers). It serves as a middle-ground benchmark
 * between native Jersey and Spring Data REST.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class Application {

    /**
     * Bootstraps the Variant C Spring Boot application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("🚀 Starting Variant C - Spring MVC + Hibernate Application");
        System.out.println("============================================================");
        SpringApplication.run(Application.class, args);
        System.out.println("✅ Variant C started successfully on http://localhost:8081");
    }
}