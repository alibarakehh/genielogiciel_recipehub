package com.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Review responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;
    private Integer rating;
    private String comment;
    private Long userId;
    private String username;
    private Long recipeId;
    private String recipeTitle;
    private LocalDateTime createdAt;
}
