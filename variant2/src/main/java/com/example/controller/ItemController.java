package com.example.controller;

import com.example.model.Item;
import com.example.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing Items in Variant C (Spring MVC).
 * <p>
 * Exposes endpoints for Item management including list, search by category,
 * and individual detail access.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    /**
     * Business logic dependency.
     */
    private final ItemService itemService;

    /**
     * Constructor injection.
     * 
     * @param itemService the service layer
     */
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * List all items with optional category filtering.
     * 
     * @param page       index
     * @param size       size
     * @param categoryId optional category filter
     * @return 200 OK with list
     */
    @GetMapping
    public ResponseEntity<Page<Item>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {

        Page<Item> items;
        if (categoryId != null) {
            items = itemService.getItemsByCategoryId(categoryId, page, size);
        } else {
            items = itemService.getAllItems(page, size);
        }
        return ResponseEntity.ok(items);
    }

    /**
     * Detail retrieval.
     * 
     * @param id unique key
     * @return 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    /**
     * Persistence creation.
     * 
     * @param item data payload
     * @return 201 CREATED
     */
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item created = itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Selective update.
     * 
     * @param id   key
     * @param item new properties
     * @return 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(
            @PathVariable Long id,
            @RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItem(id, item));
    }

    /**
     * Logical removal.
     * 
     * @param id key to delete
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}