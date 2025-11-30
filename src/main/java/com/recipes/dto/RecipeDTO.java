package com.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for Recipe responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    private Long id;
    private String title;
    private String description;
    private String instructions;
    private String imageUrl;
    private Integer prepTime;
    private Integer cookTime;
    private Integer totalTime;
    private Integer servings;
    private String difficulty;
    private Integer calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double fiber;
    private Double averageRating;
    private Integer reviewCount;
    private Integer viewCount;
    private Boolean published;
    private Long authorId;
    private String authorUsername;
    private Long categoryId;
    private String categoryName;
    private List<IngredientItemDTO> ingredients = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
