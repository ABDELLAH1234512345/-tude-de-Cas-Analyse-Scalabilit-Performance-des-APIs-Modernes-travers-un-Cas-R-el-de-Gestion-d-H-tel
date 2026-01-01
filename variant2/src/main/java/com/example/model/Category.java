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
 * Entity class representing a Category in Variant C (Spring MVC + Hibernate).
 * <p>
 * This entity defines the structure for product categories. It uses standard
 * JPA annotations for mapping to the SQL database.
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
     * Unique business code for the category.
     */
    @Column(unique = true, nullable = false, length = 32)
    private String code;

    /**
     * Category display name.
     */
    @Column(nullable = false, length = 128)
    private String name;

    /**
     * Timestamp for the last database update.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Bidirectional relationship with items.
     * Items are excluded from JSON output to prevent recursion.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();
}