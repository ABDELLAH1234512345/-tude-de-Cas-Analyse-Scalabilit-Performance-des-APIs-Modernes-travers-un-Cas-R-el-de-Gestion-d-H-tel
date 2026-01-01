package com.example.service;

import com.example.Category;
import com.example.Item;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service responsible for generating test data for performance benchmarking.
 * <p>
 * This service uses the JavaFaker library to generate realistic test data
 * including
 * categories and items. It creates a large dataset (2000 categories and 100,000
 * items)
 * suitable for performance testing and benchmarking REST API implementations.
 * </p>
 * 
 * <p>
 * The data generation process:
 * </p>
 * <ul>
 * <li>Checks if data already exists to avoid duplication</li>
 * <li>Generates 2000 categories with unique codes and names</li>
 * <li>Generates 100,000 items with random attributes and category
 * associations</li>
 * <li>Uses batch processing to optimize memory usage and database
 * performance</li>
 * </ul>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Service
public class DataGeneratorService {

    /**
     * Logger for tracking data generation progress and status.
     */
    private static final Logger logger = LoggerFactory.getLogger(DataGeneratorService.class);

    /**
     * Repository for Category entity persistence.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Repository for Item entity persistence.
     */
    private final ItemRepository itemRepository;

    /**
     * Faker instance configured for French locale to generate realistic test data.
     */
    private final Faker faker = new Faker(new Locale("fr"));

    /**
     * Constructor for dependency injection.
     * 
     * @param categoryRepository repository for category data access
     * @param itemRepository     repository for item data access
     */
    public DataGeneratorService(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Generates test data for categories and items if the database is empty.
     * <p>
     * This method is transactional to ensure data consistency. It performs the
     * following steps:
     * </p>
     * <ol>
     * <li>Checks if data already exists - skips generation if found</li>
     * <li>Generates 2000 categories with unique codes and department names</li>
     * <li>Generates 100,000 items with random SKUs, names, prices, and stock
     * quantities</li>
     * <li>Associates each item with a random category</li>
     * <li>Saves data in batches of 1000 items to optimize memory usage</li>
     * </ol>
     * 
     * <p>
     * <b>Performance Note:</b> This method may take several minutes to complete
     * due to the large volume of data being generated.
     * </p>
     */
    @Transactional
    public void generateData() {
        if (categoryRepository.count() > 0 || itemRepository.count() > 0) {
            // Idempotent guard: skip generation when seed data is already present to avoid duplicates
            logger.info("La base de données contient déjà des données. La génération de données est annulée.");
            return;
        }

        logger.info("Début de la génération des données...");

        // 1. Générer les catégories
        logger.info("Génération de 2000 catégories...");
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            Category category = new Category();
            String department = faker.commerce().department().replaceAll("[^a-zA-Z0-9.-]", "").toLowerCase();
            if (department.length() > 25) {
                department = department.substring(0, 25);
            }
            category.setCode(department + "_" + i);
            category.setName(faker.commerce().department());
            categories.add(category);
        }
        categoryRepository.saveAll(categories);
        logger.info("2000 catégories générées avec succès.");

        // 2. Générer les items
        logger.info("Génération de 100 000 items...");
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Item item = new Item();
            item.setSku(faker.code().ean13());
            item.setName(faker.commerce().productName());
            item.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 1000)));
            item.setStock(faker.number().numberBetween(0, 1000));
            // Associer à une catégorie aléatoire
            item.setCategory(categories.get(ThreadLocalRandom.current().nextInt(0, categories.size())));
            items.add(item);

            // Sauvegarder par lots pour éviter une consommation de mémoire excessive
            if (items.size() % 1000 == 0) {
                itemRepository.saveAll(items);
                items.clear();
                // Emit progress log every batch to monitor long-running insert phase without flooding logs
                logger.info("{} items générés...", i + 1);
            }
        }
        // Sauvegarder les items restants
        if (!items.isEmpty()) {
            itemRepository.saveAll(items);
        }
        logger.info("100 000 items générés avec succès.");
        logger.info("Génération de données terminée.");
    }
}