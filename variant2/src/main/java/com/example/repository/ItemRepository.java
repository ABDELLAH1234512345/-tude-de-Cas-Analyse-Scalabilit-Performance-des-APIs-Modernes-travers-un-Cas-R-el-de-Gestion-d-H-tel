package com.example.repository;

import com.example.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Data access interface for Item entities in Variant C.
 * <p>
 * Provides optimized queries for mass item retrieval and category-based
 * filtering,
 * implementing JOIN FETCH to prevent N+1 select issues during performance
 * benchmarks.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Finds items by category with eager loading of the category object.
     * <p>
     * Optimized for performance testing to avoid multiple queries per item.
     * </p>
     * 
     * @param categoryId filter criteria
     * @param pageable   pagination info
     * @return paginated list of items
     */
    // Query with JOIN FETCH to avoid N+1 problem
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Total count of items in a specific category.
     * 
     * @param categoryId category identifier
     * @return item count
     */
    // Count items by category
    long countByCategoryId(Long categoryId);
}