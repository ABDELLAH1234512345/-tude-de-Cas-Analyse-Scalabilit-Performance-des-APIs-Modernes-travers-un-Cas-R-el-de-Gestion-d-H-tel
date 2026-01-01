package com.example.service;

import com.example.dao.CategoryDAO;
import com.example.dao.ItemDAO;
import com.example.model.Category;
import com.example.model.Item;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service responsible for generating massive test data for Variant A
 * benchmarks.
 * <p>
 * This class populates the database with 2,000 categories and 100,000 items.
 * It uses JavaFaker for realistic data and implements batch processing to
 * optimize database performance and memory usage during the generation process.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class DataGeneratorService {

    /**
     * Faker instance for generating realistic random data strings.
     */
    private static final Faker faker = new Faker();

    /**
     * Random generator for numeric attributes and associations.
     */
    private static final Random random = new Random();

    /**
     * Entry point for data generation.
     * Checks existing data count before starting to avoid duplicates.
     */
    public static void generateData() {
        CategoryDAO categoryDAO = new CategoryDAO();
        ItemDAO itemDAO = new ItemDAO();

        long categoryCount = categoryDAO.count();
        long itemCount = itemDAO.count();

        System.out.println("📊 Current database state:");
        System.out.println("   Categories: " + categoryCount);
        System.out.println("   Items: " + itemCount);

        if (categoryCount > 0 && itemCount > 0) {
            System.out.println("✅ Data already exists. Skipping generation.");
            return;
        }

        System.out.println("🚀 Starting data generation...");
        long startTime = System.currentTimeMillis();

        // Generate 2000 categories
        System.out.println("📦 Generating 2000 categories...");
        List<Category> categories = generateCategories(2000);
        categoryDAO.saveAll(categories);
        System.out.println("✅ Categories saved");

        // Generate 100,000 items
        System.out.println("📦 Generating 100,000 items...");
        generateAndSaveItemsInBatches(itemDAO, categories, 100000, 5000);

        long endTime = System.currentTimeMillis();
        System.out.println("✅ Data generation completed in " + (endTime - startTime) / 1000 + " seconds");
        System.out.println("📊 Final database state:");
        System.out.println("   Categories: " + categoryDAO.count());
        System.out.println("   Items: " + itemDAO.count());
    }

    /**
     * Creates a list of categories with various types.
     * 
     * @param count number of categories to generate
     * @return list of generated categories
     */
    private static List<Category> generateCategories(int count) {
        List<Category> categories = new ArrayList<>();
        String[] categoryTypes = { "Electronics", "Clothing", "Food", "Books", "Toys",
                "Sports", "Home", "Garden", "Beauty", "Automotive" };

        for (int i = 1; i <= count; i++) {
            Category category = new Category();
            category.setCode(categoryTypes[i % categoryTypes.length].toLowerCase() + "_" + i);
            category.setName(categoryTypes[i % categoryTypes.length] + " Category " + i);
            categories.add(category);

            if (i % 500 == 0) {
                System.out.println("   Generated " + i + " categories...");
            }
        }
        return categories;
    }

    /**
     * Generates items in batches to optimize database I/O and memory.
     * 
     * @param itemDAO    DAO to use for saving
     * @param categories list of available categories for association
     * @param totalItems total number of items to generate
     * @param batchSize  size of each save batch
     */
    private static void generateAndSaveItemsInBatches(ItemDAO itemDAO, List<Category> categories,
            int totalItems, int batchSize) {
        int batches = totalItems / batchSize;

        for (int batch = 0; batch < batches; batch++) {
            List<Item> items = new ArrayList<>();
            int startIndex = batch * batchSize;

            for (int i = 0; i < batchSize; i++) {
                int itemNumber = startIndex + i + 1;
                Item item = new Item();
                item.setSku("ITEM_" + itemNumber);
                item.setName(faker.commerce().productName() + " #" + itemNumber);
                item.setDescription(faker.lorem().sentence(15));
                item.setPrice(BigDecimal.valueOf(random.nextDouble() * 1000).setScale(2, BigDecimal.ROUND_HALF_UP));
                item.setStock(random.nextInt(1000));
                item.setCategory(categories.get(random.nextInt(categories.size())));
                items.add(item);
            }

            itemDAO.saveAll(items);
            System.out.println(
                    "   Saved batch " + (batch + 1) + "/" + batches + " (" + ((batch + 1) * batchSize) + " items)");
        }
    }
}
