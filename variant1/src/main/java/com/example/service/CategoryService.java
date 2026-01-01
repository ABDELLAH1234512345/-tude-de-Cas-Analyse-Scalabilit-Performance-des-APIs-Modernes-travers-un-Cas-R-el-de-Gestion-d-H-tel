package com.example.service;

import com.example.dao.CategoryDAO;
import com.example.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Category business logic (Variant A).
 * <p>
 * This service acts as a bridge between the Resource layer and the DAO layer.
 * It handles category retrieval, creation, updates, and deletion.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class CategoryService {

    /**
     * DAO instance for category data operations.
     */
    private final CategoryDAO categoryDAO;

    /**
     * Default constructor initializing the DAO.
     */
    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    /**
     * Creates a new category.
     * 
     * @param category category to persist
     * @return the saved category
     */
    public Category createCategory(Category category) {
        return categoryDAO.save(category);
    }

    /**
     * Retrieves a category by ID.
     * 
     * @param id primary key
     * @return Optional containing the category
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryDAO.findById(id);
    }

    /**
     * Retrieves a paginated list of all categories.
     * 
     * @param page page number
     * @param size page size
     * @return list of categories
     */
    public List<Category> getAllCategories(int page, int size) {
        return categoryDAO.findAll(page, size);
    }

    /**
     * Gets the total count of categories.
     * 
     * @return total count
     */
    public long countCategories() {
        return categoryDAO.count();
    }

    /**
     * Updates an existing category.
     * 
     * @param id       ID of the category to update
     * @param category new category data
     * @return updated category
     * @throws RuntimeException if category not found
     */
    public Category updateCategory(Long id, Category category) {
        if (!categoryDAO.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        category.setId(id);
        return categoryDAO.update(category);
    }

    /**
     * Deletes a category by ID.
     * 
     * @param id ID to delete
     * @throws RuntimeException if category not found
     */
    public void deleteCategory(Long id) {
        if (!categoryDAO.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryDAO.delete(id);
    }
}
