package com.example.service;

import com.example.dao.ItemDAO;
import com.example.model.Item;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Item business logic (Variant A).
 * <p>
 * This service manages item operations including CRUD actions and
 * category-based filtering. It uses the ItemDAO for database interactions.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class ItemService {

    /**
     * DAO instance for item data operations.
     */
    private final ItemDAO itemDAO;

    /**
     * Default constructor initializing the DAO.
     */
    public ItemService() {
        this.itemDAO = new ItemDAO();
    }

    /**
     * Creates a new item.
     * 
     * @param item item to persist
     * @return the saved item
     */
    public Item createItem(Item item) {
        return itemDAO.save(item);
    }

    /**
     * Retrieves an item by its unique ID.
     * 
     * @param id primary key
     * @return Optional containing the item
     */
    public Optional<Item> getItemById(Long id) {
        return itemDAO.findById(id);
    }

    /**
     * Retrieves a paginated list of all items.
     * 
     * @param page page index
     * @param size page size
     * @return list of items
     */
    public List<Item> getAllItems(int page, int size) {
        return itemDAO.findAll(page, size);
    }

    /**
     * Retrieves items filtered by category with pagination.
     * 
     * @param categoryId category identifier
     * @param page       page index
     * @param size       page size
     * @return list of items in the category
     */
    public List<Item> getItemsByCategoryId(Long categoryId, int page, int size) {
        return itemDAO.findByCategoryId(categoryId, page, size);
    }

    /**
     * Gets the total count of items in the database.
     * 
     * @return total count
     */
    public long countItems() {
        return itemDAO.count();
    }

    /**
     * Counts items belonging to a specific category.
     * 
     * @param categoryId category identifier
     * @return count of items in the category
     */
    public long countItemsByCategoryId(Long categoryId) {
        return itemDAO.countByCategoryId(categoryId);
    }

    /**
     * Updates an existing item.
     * 
     * @param id   identifier of the item to update
     * @param item new item data
     * @return the updated item
     * @throws RuntimeException if item is not found
     */
    public Item updateItem(Long id, Item item) {
        if (!itemDAO.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        item.setId(id);
        return itemDAO.update(item);
    }

    /**
     * Deletes an item by its identifier.
     * 
     * @param id unique identifier to delete
     * @throws RuntimeException if item is not found
     */
    public void deleteItem(Long id) {
        if (!itemDAO.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        itemDAO.delete(id);
    }
}
