package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a product item in the benchmark database.
 * <p>
 * This entity represents individual products that can be managed through the
 * REST API.
 * Each item belongs to a category and contains essential product information
 * such as
 * SKU, name, price, and stock quantity. The class uses JPA annotations for ORM
 * mapping
 * and Lombok annotations to reduce boilerplate code.
 * </p>
 * 
 * <p>
 * <b>Database Table:</b> item
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    /**
     * Unique identifier for the item.
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Stock Keeping Unit (SKU) - unique identifier for inventory management.
     * Used for business logic and external system integration.
     * Maximum length: 64 characters.
     */
    @Column(unique = true, nullable = false, length = 64)
    private String sku; // Stock Keeping Unit

    /**
     * Display name of the item.
     * Maximum length: 128 characters.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Price of the item in the system's base currency.
     * <p>
     * Uses BigDecimal for precise monetary calculations.
     * Precision: 10 digits total, 2 decimal places.
     * </p>
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Current stock quantity available for this item.
     * Represents the number of units in inventory.
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     * Category to which this item belongs.
     * <p>
     * Many-to-one relationship with the Category entity. Uses lazy loading to
     * optimize
     * performance. JsonIgnoreProperties prevents infinite recursion during JSON
     * serialization.
     * This field is optional (nullable = true) to allow items without a category.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    @JsonIgnoreProperties("items") // Éviter la sérialisation infinie
    private Category category; // Relation vers la catégorie

    /**
     * Timestamp of the last update to this item.
     * Automatically updated by Hibernate on each modification.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Explicit setter for identifier to avoid relying solely on Lombok in IDEs where
     * annotation processing may be misconfigured.
     *
     * @param id item identifier to assign
     */
    public void setId(Long id) {
        this.id = id;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
