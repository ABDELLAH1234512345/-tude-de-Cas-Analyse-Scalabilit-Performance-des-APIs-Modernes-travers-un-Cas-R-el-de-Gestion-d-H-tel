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
 * Entity class representing an Item in Variant D (Spring Data REST).
 * <p>
 * This entity represents items in the most automated variant.
 * Data is exposed through automated HATEOAS links.
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
     * Unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Stock identifier.
     */
    @Column(unique = true, nullable = false, length = 64)
    private String sku;

    /**
     * Product name.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Product description.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Product price.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Inventory count.
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     * DB change timestamp.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Parent category.
     * Ignored to simplify JSON payloads for automated benchmarks.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}