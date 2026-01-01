package com.example.service;

import com.example.model.Category;
import com.example.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for Category business logic in Variant C.
 * <p>
 * This class provides transactional methods to manage categories
 * through the {@link CategoryRepository}.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
public class CategoryService {

    /**
     * Repository dependency.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Constructor injection.
     * 
     * @param categoryRepository the repository to use
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all categories with pagination.
     * 
     * @param page index
     * @param size page size
     * @return paginated categories
     */
    public Page<Category> getAllCategories(int page, int size) {
        return categoryRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Fetches a single category or throws an exception.
     * 
     * @param id category ID
     * @return the found category
     * @throws RuntimeException if not found
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    /**
     * Persists a new category.
     * 
     * @param category data to save
     * @return saved entity
     */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Updates an existing category.
     * 
     * @param id       primary key
     * @param category updated data
     * @return updated entity
     */
    public Category updateCategory(Long id, Category category) {
        Category existing = getCategoryById(id);
        existing.setCode(category.getCode());
        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    /**
     * Removes a category.
     * 
     * @param id identifier to delete
     */
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Total count of categories.
     * 
     * @return count
     */
    public long countCategories() {
        return categoryRepository.count();
    }
}