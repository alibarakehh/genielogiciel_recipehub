package com.recipes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity class representing a Recipe
 * 
 * Core entity of the application containing all recipe information
 * including ingredients, instructions, nutritional data, and relationships.
 */
@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private Integer prepTime; // in minutes

    @Column(nullable = false)
    private Integer cookTime; // in minutes

    @Column(nullable = false)
    private Integer servings;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty = Difficulty.MEDIUM;

    // Nutritional Information
    @Column
    private Integer calories;

    @Column
    private Double protein; // grams

    @Column
    private Double carbohydrates; // grams

    @Column
    private Double fat; // grams

    @Column
    private Double fiber; // grams

    // Statistics
    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private Integer reviewCount = 0;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @Column(nullable = false)
    private Boolean published = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(mappedBy = "favoriteRecipes")
    private Set<User> favoritedBy = new HashSet<>();

    @ManyToMany(mappedBy = "recipes")
    private Set<Collection> collections = new HashSet<>();

    // Enum for difficulty levels
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    // Helper method to calculate total time
    public Integer getTotalTime() {
        return prepTime + cookTime;
    }
}
