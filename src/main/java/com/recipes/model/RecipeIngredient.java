package com.recipes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Join entity representing the relationship between Recipe and Ingredient
 * 
 * This entity stores the quantity and unit for each ingredient in a recipe
 */
@Entity
@Table(name = "recipe_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false, length = 50)
    private String unit; // e.g., "cup", "tablespoon", "gram", "ml"

    @Column(length = 200)
    private String notes; // e.g., "finely chopped", "optional"

    @Column(nullable = false)
    private Integer displayOrder = 0; // For ordering ingredients in the recipe
}
