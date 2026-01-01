package com.example.repository;

import com.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access interface for Category entities in Variant C.
 * <p>
 * Uses Spring Data JPA to provide standard CRUD operations.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}