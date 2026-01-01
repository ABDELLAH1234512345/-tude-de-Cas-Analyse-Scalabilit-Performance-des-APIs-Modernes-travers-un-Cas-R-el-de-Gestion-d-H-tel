package com.example.controller;

import com.example.Category;
import com.example.Item; // Importation de Item pour la méthode getItemsByCategoryId
import com.example.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST Controller for managing Category resources.
 * <p>
 * This controller provides CRUD operations and additional endpoints for
 * managing
 * product categories. All endpoints support pagination where applicable and
 * return
 * appropriate HTTP status codes.
 * </p>
 * 
 * <p>
 * <b>Base Path:</b> /categories
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    /**
     * Repository for Category entity data access.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for dependency injection.
     * 
     * @param categoryRepository the repository for category data access
     */
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all categories with pagination support.
     * 
     * @param page the page number (zero-based), default is 0
     * @param size the number of items per page, default is 10
     * @return ResponseEntity containing a page of categories with HTTP 200 OK
     *         status
     */
    // GET /categories?page=&size=
    @GetMapping
    public ResponseEntity<Page<Category>> getAllCategories(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Retrieves a specific category by its ID.
     * 
     * @param id the unique identifier of the category
     * @return ResponseEntity containing the category with HTTP 200 OK if found,
     *         or HTTP 404 NOT FOUND if the category doesn't exist
     */
    // GET /categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Creates a new category.
     * 
     * @param category the category object to create (from request body)
     * @return ResponseEntity containing the created category with HTTP 201 CREATED
     *         status
     */
    // POST /categories
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    /**
     * Updates an existing category.
     * 
     * @param id       the unique identifier of the category to update
     * @param category the updated category data (from request body)
     * @return ResponseEntity containing the updated category with HTTP 200 OK if
     *         successful,
     *         or HTTP 404 NOT FOUND if the category doesn't exist
     */
    // PUT /categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        if (!categoryRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        category.setId(id); // Assurez-vous que l'ID de la catégorie à mettre à jour est correct
        Category updatedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    /**
     * Deletes a category by its ID.
     * 
     * @param id the unique identifier of the category to delete
     * @return ResponseEntity with HTTP 204 NO CONTENT if successful,
     *         or HTTP 404 NOT FOUND if the category doesn't exist
     */
    // DELETE /categories/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves all items belonging to a specific category with pagination.
     * <p>
     * This endpoint demonstrates the inverse relationship between Category and Item
     * entities.
     * It uses a custom repository method to efficiently fetch items for a given
     * category.
     * </p>
     * 
     * @param id   the unique identifier of the category
     * @param page the page number (zero-based), default is 0
     * @param size the number of items per page, default is 10
     * @return ResponseEntity containing a page of items with HTTP 200 OK if the
     *         category exists,
     *         or HTTP 404 NOT FOUND if the category doesn't exist
     */
    // GET /categories/{id}/items (association inverse avec pagination)
    @GetMapping("/{id}/items")
    public ResponseEntity<Page<Item>> getItemsByCategoryId(@PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (!categoryRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = categoryRepository.findItemsByCategoryId(id, pageable);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
