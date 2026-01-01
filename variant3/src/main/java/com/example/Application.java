package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Variant D: Spring Data REST.
 * <p>
 * This variant represents the "Low Code" approach where the entire REST API
 * is generated automatically from repositories. It provides full HATEOAS
 * support by default.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class Application {

    /**
     * Entry point for the Variant D application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("🚀 Starting Variant D - Spring Data REST Application");
        System.out.println("============================================================");
        SpringApplication.run(Application.class, args);
        System.out.println("✅ Variant D started successfully on http://localhost:8082");
        System.out.println("📚 HATEOAS endpoints available at root: http://localhost:8082/");
    }
}
