package com.example;

import com.example.service.DataGeneratorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component responsible for initializing the database with test data on
 * application startup.
 * <p>
 * This class implements CommandLineRunner to execute data generation logic
 * after the Spring
 * application context has been fully initialized. It delegates the actual data
 * generation
 * to the DataGeneratorService.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 * @see DataGeneratorService
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /**
     * Service responsible for generating test data.
     */
    private final DataGeneratorService dataGeneratorService;

    /**
     * Constructor for dependency injection.
     * 
     * @param dataGeneratorService the service that generates test data
     */
    public DataInitializer(DataGeneratorService dataGeneratorService) {
        this.dataGeneratorService = dataGeneratorService;
    }

    /**
     * Callback method executed after the application context is loaded.
     * <p>
     * This method triggers the generation of test data by calling the
     * DataGeneratorService. It runs automatically on application startup.
     * </p>
     * 
     * @param args command-line arguments passed to the application
     * @throws Exception if an error occurs during data generation
     */
    @Override
    public void run(String... args) throws Exception {
        dataGeneratorService.generateData();
    }
}