package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class representing a product category in the benchmark database.
 * <p>
 * This entity is used to organize items into logical groups. Each category can
 * contain
 * multiple items through a one-to-many relationship. The class uses JPA
 * annotations for
 * ORM mapping and Lombok annotations to reduce boilerplate code.
 * </p>
 * 
 * <p>
 * <b>Database Table:</b> category
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Unique identifier for the category.
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique code identifier for the category.
     * Used for business logic and external references.
     * Maximum length: 32 characters.
     */
    @Column(unique = true, nullable = false, length = 32)
    private String code;

    /**
     * Display name of the category.
     * Maximum length: 128 characters.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Timestamp of the last update to this category.
     * Automatically updated by Hibernate on each modification.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * List of items belonging to this category.
     * <p>
     * This is the inverse side of the many-to-one relationship defined in the Item
     * entity.
     * Uses lazy loading to optimize performance and avoid unnecessary database
     * queries.
     * JsonIgnoreProperties prevents infinite recursion during JSON serialization.
     * </p>
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("category") // Éviter la sérialisation infinie
    private List<Item> items; // Relation inverse : Liste des items

    /**
     * Explicit setter for identifier to avoid relying solely on Lombok in IDEs where
     * annotation processing may be misconfigured.
     *
     * @param id category identifier to assign
     */
    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
