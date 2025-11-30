package com.recipes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating/updating recipes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    private String imageUrl;

    @NotNull(message = "Preparation time is required")
    @Positive(message = "Preparation time must be positive")
    private Integer prepTime;

    @NotNull(message = "Cooking time is required")
    @Positive(message = "Cooking time must be positive")
    private Integer cookTime;

    @NotNull(message = "Servings is required")
    @Positive(message = "Servings must be positive")
    private Integer servings;

    @NotBlank(message = "Difficulty is required")
    private String difficulty; // EASY, MEDIUM, HARD

    private Long categoryId;

    // Nutritional information (optional)
    private Integer calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double fiber;

    private Boolean published = false;

    private List<IngredientItemDTO> ingredients = new ArrayList<>();
}
