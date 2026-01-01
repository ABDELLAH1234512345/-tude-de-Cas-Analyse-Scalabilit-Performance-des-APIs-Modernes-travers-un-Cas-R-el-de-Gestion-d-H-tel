package com.example.controller;

import com.example.model.Category;
import com.example.model.Item;
import com.example.service.CategoryService;
import com.example.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing Categories in Variant C (Spring MVC).
 * <p>
 * Provides endpoints for Category CRUD operations and sub-resource
 * navigation to items.
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
     * Category service layer.
     */
    private final CategoryService categoryService;

    /**
     * Item service layer for sub-resource access.
     */
    private final ItemService itemService;

    /**
     * Constructor injection.
     * 
     * @param categoryService the category service
     * @param itemService     the item service
     */
    public CategoryController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    /**
     * Paginated retrieval of all categories.
     * 
     * @param page index
     * @param size size
     * @return 200 OK with page
     */
    @GetMapping
    public ResponseEntity<Page<Category>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size));
    }

    /**
     * Single category retrieval.
     * 
     * @param id key
     * @return 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    /**
     * Items belonging to a category.
     * 
     * @param id   category ID
     * @param page index
     * @param size size
     * @return paginated items
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<Page<Item>> getItemsByCategoryId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Item> items = itemService.getItemsByCategoryId(id, page, size);
        return ResponseEntity.ok(items);
    }

    /**
     * Resource creation.
     * 
     * @param category data
     * @return 201 CREATED
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Resource update.
     * 
     * @param id       key
     * @param category new data
     * @return 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    /**
     * Resource deletion.
     * 
     * @param id key
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}