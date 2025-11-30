package com.recipes.service;

import com.recipes.dto.ReviewDTO;
import com.recipes.dto.ReviewRequest;
import com.recipes.exception.BadRequestException;
import com.recipes.exception.ResourceNotFoundException;
import com.recipes.model.Recipe;
import com.recipes.model.Review;
import com.recipes.model.User;
import com.recipes.repository.RecipeRepository;
import com.recipes.repository.ReviewRepository;
import com.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for review and rating operations
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewDTO createReview(Long recipeId, ReviewRequest reviewRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        // Check if user already reviewed this recipe
        if (reviewRepository.existsByUserIdAndRecipeId(user.getId(), recipeId)) {
            throw new BadRequestException("You have already reviewed this recipe");
        }

        Review review = new Review();
        review.setUser(user);
        review.setRecipe(recipe);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        Review savedReview = reviewRepository.save(review);

        // Update recipe statistics
        updateRecipeRating(recipeId);

        return convertToDTO(savedReview);
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!review.getUser().getUsername().equals(username)) {
            throw new BadRequestException("You can only update your own reviews");
        }

        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        Review updatedReview = reviewRepository.save(review);

        // Update recipe statistics
        updateRecipeRating(review.getRecipe().getId());

        return convertToDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!review.getUser().getUsername().equals(username)) {
            throw new BadRequestException("You can only delete your own reviews");
        }

        Long recipeId = review.getRecipe().getId();
        reviewRepository.delete(review);

        // Update recipe statistics
        updateRecipeRating(recipeId);
    }

    public Page<ReviewDTO> getReviewsByRecipe(Long recipeId, Pageable pageable) {
        return reviewRepository.findByRecipeId(recipeId, pageable)
                .map(this::convertToDTO);
    }

    public Page<ReviewDTO> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(this::convertToDTO);
    }

    private void updateRecipeRating(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        Double averageRating = reviewRepository.getAverageRatingByRecipeId(recipeId);
        Long reviewCount = reviewRepository.countByRecipeId(recipeId);

        recipe.setAverageRating(averageRating != null ? averageRating : 0.0);
        recipe.setReviewCount(reviewCount.intValue());

        recipeRepository.save(recipe);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setRecipeId(review.getRecipe().getId());
        dto.setRecipeTitle(review.getRecipe().getTitle());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
