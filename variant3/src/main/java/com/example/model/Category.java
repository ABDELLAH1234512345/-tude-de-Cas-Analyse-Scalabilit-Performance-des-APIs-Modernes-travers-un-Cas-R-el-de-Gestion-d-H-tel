package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a Category in Variant D (Spring Data REST).
 * <p>
 * This entity defines categories used in the fully automated Spring Data REST
 * variant.
 * It follows the same structural pattern as other variants but is exposed
 * automatically via HATEOAS.
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
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique business code.
     */
    @Column(unique = true, nullable = false, length = 32)
    private String code;

    /**
     * Category name.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Automatic update timestamp.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Link to items.
     * Ignored in JSON to prevent circular references in automated HATEOAS.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();
}