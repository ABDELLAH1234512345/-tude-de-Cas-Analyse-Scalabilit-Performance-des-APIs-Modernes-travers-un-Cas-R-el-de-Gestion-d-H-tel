package com.example.repository;

import com.example.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Automated REST repository for Item entities in Variant D.
 * <p>
 * Automatically exposes REST endpoints for Item management.
 * Includes custom HATEOAS search resources for category filtering.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@RepositoryRestResource(path = "items", collectionResourceRel = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Custom search endpoint to filter items by category with JOIN FETCH.
     * <p>
     * Exposed at /items/search/by-category?categoryId=...
     * </p>
     * 
     * @param categoryId filter criteria
     * @param pageable   pagination info
     * @return paginated entities
     */
    // Query with JOIN FETCH to avoid N+1 problem
    @RestResource(path = "by-category", rel = "by-category")
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}