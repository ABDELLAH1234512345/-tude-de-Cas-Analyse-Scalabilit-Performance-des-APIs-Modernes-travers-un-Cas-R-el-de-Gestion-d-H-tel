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
 * Entity class representing a Category for the benchmarks.
 * <p>
 * This entity is mapped to the 'category' table using JPA annotations.
 * It provides a list of associated items through a lazy-loaded one-to-many
 * relationship.
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
     * Primary key for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique code identifying the category (max 32 chars).
     */
    @Column(unique = true, nullable = false, length = 32)
    private String code;

    /**
     * Human-readable name of the category (max 128 chars).
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Timestamp of the last update, managed by Hibernate.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Collection of items belonging to this category.
     * Ignored in JSON to avoid cyclic dependencies.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();
}
