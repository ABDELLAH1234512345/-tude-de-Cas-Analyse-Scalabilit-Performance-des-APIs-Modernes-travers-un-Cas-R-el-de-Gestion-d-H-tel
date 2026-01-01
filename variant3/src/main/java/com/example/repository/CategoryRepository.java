package com.example.repository;

import com.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Automated REST repository for Category entities in Variant D.
 * <p>
 * Leverages Spring Data REST to automatically expose a HATEOAS-compliant
 * REST API for category management.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@RepositoryRestResource(path = "categories", collectionResourceRel = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}