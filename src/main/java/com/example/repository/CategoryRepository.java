package com.example.repository;

import com.example.Category;
import com.example.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Category entity data access.
 * <p>
 * This repository extends JpaRepository to provide standard CRUD operations
 * and custom query methods for Category entities. It includes optimized queries
 * for retrieving related items with pagination support.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Repository
// @RepositoryRestResource(collectionResourceRel = "categories", path =
// "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Retrieves all items belonging to a specific category with pagination.
     * <p>
     * This method uses JPQL to query items by category ID. It supports pagination
     * to handle large result sets efficiently.
     * </p>
     * 
     * @param categoryId the ID of the category
     * @param pageable   pagination information (page number, size, sorting)
     * @return a page of items belonging to the specified category
     */
    // Méthode pour récupérer les items d'une catégorie avec pagination
    @Query("SELECT i FROM Item i WHERE i.category.id = :categoryId")
    Page<Item> findItemsByCategoryId(Long categoryId, Pageable pageable);
}
