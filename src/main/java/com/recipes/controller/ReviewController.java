package com.recipes.controller;

import com.recipes.dto.ReviewDTO;
import com.recipes.dto.ReviewRequest;
import com.recipes.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for reviews and ratings
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Recipe reviews and ratings")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/recipe/{recipeId}")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Create a review for a recipe")
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long recipeId,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewDTO review = reviewService.createReview(recipeId, reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{reviewId}")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Update a review")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewDTO review = reviewService.updateReview(reviewId, reviewRequest);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Delete a review")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recipe/{recipeId}")
    @Operation(summary = "Get reviews for a recipe")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByRecipe(
            @PathVariable Long recipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDTO> reviews = reviewService.getReviewsByRecipe(recipeId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reviews by a user")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDTO> reviews = reviewService.getReviewsByUser(userId, pageable);
        return ResponseEntity.ok(reviews);
    }
}
