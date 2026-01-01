package com.example.controller;

import com.example.Item;
import com.example.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST Controller for managing Item resources.
 * <p>
 * This controller provides CRUD operations and filtering capabilities for
 * managing
 * product items. All endpoints support pagination where applicable and return
 * appropriate HTTP status codes. Special attention is given to avoiding N+1
 * query
 * problems through optimized repository methods.
 * </p>
 * 
 * <p>
 * <b>Base Path:</b> /items
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
     * Repository for Item entity data access.
     */
    private final ItemRepository itemRepository;

    /**
     * Constructor for dependency injection.
     * 
     * @param itemRepository the repository for item data access
     */
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Retrieves all items with pagination support.
     * 
     * @param page the page number (zero-based), default is 0
     * @param size the number of items per page, default is 10
     * @return ResponseEntity containing a page of items with HTTP 200 OK status
     */
    // GET /items?page=&size=
    @GetMapping
    public ResponseEntity<Page<Item>> getAllItems(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemRepository.findAll(pageable);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    /**
     * Retrieves a specific item by its ID.
     * 
     * @param id the unique identifier of the item
     * @return ResponseEntity containing the item with HTTP 200 OK if found,
     *         or HTTP 404 NOT FOUND if the item doesn't exist
     */
    // GET /items/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves items filtered by category ID with pagination.
     * <p>
     * This endpoint uses an optimized repository method with fetch join to avoid
     * the N+1 query problem. The category is eagerly loaded with each item to
     * minimize database round trips.
     * </p>
     * 
     * @param categoryId the ID of the category to filter by
     * @param page       the page number (zero-based), default is 0
     * @param size       the number of items per page, default is 10
     * @return ResponseEntity containing a page of items with HTTP 200 OK status
     */
    // GET /items?categoryId=...
    // Utilise la méthode anti-N+1 du repository pour charger la catégorie avec
    // l'item
    @GetMapping(params = "categoryId")
    public ResponseEntity<Page<Item>> getItemsByCategoryId(@RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Utilisation de la méthode avec fetch join pour éviter le problème N+1
        Page<Item> items = itemRepository.findByCategoryIdWithCategory(categoryId, pageable);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    /**
     * Creates a new item.
     * 
     * @param item the item object to create (from request body)
     * @return ResponseEntity containing the created item with HTTP 201 CREATED
     *         status
     */
    // POST /items
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemRepository.save(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    /**
     * Updates an existing item.
     * 
     * @param id   the unique identifier of the item to update
     * @param item the updated item data (from request body)
     * @return ResponseEntity containing the updated item with HTTP 200 OK if
     *         successful,
     *         or HTTP 404 NOT FOUND if the item doesn't exist
     */
    // PUT /items/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        if (!itemRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        item.setId(id); // Assurez-vous que l'ID de l'item à mettre à jour est correct
        Item updatedItem = itemRepository.save(item);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    /**
     * Deletes an item by its ID.
     * 
     * @param id the unique identifier of the item to delete
     * @return ResponseEntity with HTTP 204 NO CONTENT if successful,
     *         or HTTP 404 NOT FOUND if the item doesn't exist
     */
    // DELETE /items/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
        if (!itemRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        itemRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
