package com.example.repository;

import com.example.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Item entity data access.
 * <p>
 * This repository extends JpaRepository to provide standard CRUD operations
 * and custom query methods for Item entities. It includes optimized queries
 * with fetch joins to prevent N+1 query problems when loading related
 * categories.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Repository
// @RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Finds items by category ID with pagination.
     * <p>
     * This is a standard Spring Data JPA derived query method. Note that this
     * may cause N+1 query problems if the category relationship is accessed.
     * Consider using {@link #findByCategoryIdWithCategory(Long, Pageable)} instead.
     * </p>
     * 
     * @param categoryId the ID of the category to filter by
     * @param pageable   pagination information (page number, size, sorting)
     * @return a page of items belonging to the specified category
     */
    // Méthode pour filtrer les items par categoryId avec pagination
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Finds items by category ID with eager loading of the category relationship.
     * <p>
     * This method uses a JPQL query with fetch join to load the category along with
     * each item in a single query, preventing the N+1 query problem. This is the
     * recommended method for filtering items by category when the category data
     * will be accessed.
     * </p>
     * 
     * @param categoryId the ID of the category to filter by
     * @param pageable   pagination information (page number, size, sorting)
     * @return a page of items with their categories eagerly loaded
     */
    // Variante anti-N+1 pour le filtre par categoryId avec fetch join
    @Query("select i from Item i join fetch i.category where i.category.id = :categoryId")
    Page<Item> findByCategoryIdWithCategory(Long categoryId, Pageable pageable);
}
