package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing an Item in Variant C (Spring MVC + Hibernate).
 * <p>
 * This entity represents product entries with SKU and pricing information.
 * It is associated with a single Category.
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
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique Stock Keeping Unit (SKU) identifying the item.
     */
    @Column(unique = true, nullable = false, length = 64)
    private String sku;

    /**
     * Name of the product.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Multi-line product description.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Selling price of the item.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Available stock quantity.
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     * Last update timestamp.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * The category this item belongs to.
     * Category is excluded from JSON list output to optimize performance.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}