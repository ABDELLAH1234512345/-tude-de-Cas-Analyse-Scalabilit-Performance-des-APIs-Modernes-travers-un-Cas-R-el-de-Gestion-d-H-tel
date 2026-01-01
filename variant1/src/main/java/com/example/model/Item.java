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
 * Entity class representing an Item for the benchmarks.
 * <p>
 * This entity represents product items with SKU, price, and stock info.
 * It is mapped to the 'item' table and belongs to a specific Category.
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
     * Primary key for the item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Stock Keeping Unit (unique identifier, max 64 chars).
     */
    @Column(unique = true, nullable = false, length = 64)
    private String sku; // Stock Keeping Unit (renamed from 'code' for consistency with Spring variants)

    /**
     * Name of the item (max 128 chars).
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Detailed description of the item.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Price of the item (precision 10, scale 2).
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Current stock level.
     */
    @Column(nullable = false)
    private Integer stock; // Renamed from 'stockQuantity' for consistency with Spring variants

    /**
     * Timestamp of the last update, managed by Hibernate.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Category this item belongs to.
     * Ignored in JSON output to minimize payload for heavy-read benchmarks.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
