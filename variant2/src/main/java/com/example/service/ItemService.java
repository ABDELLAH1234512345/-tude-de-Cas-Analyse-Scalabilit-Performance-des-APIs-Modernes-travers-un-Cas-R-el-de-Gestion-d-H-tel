package com.example.service;

import com.example.model.Item;
import com.example.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for Item business logic in Variant C.
 * <p>
 * This class provides transactional methods to manage items and their
 * associations with categories using the {@link ItemRepository}.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
public class ItemService {

    /**
     * Repository dependency.
     */
    private final ItemRepository itemRepository;

    /**
     * Constructor injection.
     * 
     * @param itemRepository the repository to use
     */
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Retrieves all items with pagination.
     * 
     * @param page index
     * @param size capacity
     * @return paginated list
     */
    public Page<Item> getAllItems(int page, int size) {
        return itemRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Finds a single item by ID.
     * 
     * @param id identifier
     * @return the found item
     * @throws RuntimeException if missing
     */
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    /**
     * Retrieves items belonging to a specific category.
     * 
     * @param categoryId filter criteria
     * @param page       index
     * @param size       capacity
     * @return paginated results
     */
    public Page<Item> getItemsByCategoryId(Long categoryId, int page, int size) {
        return itemRepository.findByCategoryId(categoryId, PageRequest.of(page, size));
    }

    /**
     * Counts items in a category.
     * 
     * @param categoryId filter
     * @return item count
     */
    public long countItemsByCategoryId(Long categoryId) {
        return itemRepository.countByCategoryId(categoryId);
    }

    /**
     * Persists a new item.
     * 
     * @param item data
     * @return saved entity
     */
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    /**
     * Updates an existing item and its metadata.
     * 
     * @param id   key
     * @param item updated values
     * @return persist result
     */
    public Item updateItem(Long id, Item item) {
        Item existing = getItemById(id);
        existing.setSku(item.getSku());
        existing.setName(item.getName());
        existing.setDescription(item.getDescription());
        existing.setPrice(item.getPrice());
        existing.setStock(item.getStock());
        existing.setCategory(item.getCategory());
        return itemRepository.save(existing);
    }

    /**
     * Deletes an item from storage.
     * 
     * @param id key to remove
     */
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    /**
     * Total system-wide item count.
     * 
     * @return count
     */
    public long countItems() {
        return itemRepository.count();
    }
}