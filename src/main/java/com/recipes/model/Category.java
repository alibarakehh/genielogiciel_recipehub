package com.recipes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a Recipe Category
 * 
 * Categories help organize recipes (e.g., Desserts, Main Courses, Breakfast)
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String iconUrl;

    // Relationships
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Recipe> recipes = new HashSet<>();
}
