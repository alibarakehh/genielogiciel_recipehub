package com.recipes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for ingredient items in recipes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientItemDTO {

    private Long ingredientId;
    
    private String ingredientName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double quantity;

    @NotNull(message = "Unit is required")
    private String unit;

    private String notes;

    private Integer displayOrder;
}
