package com.recipes.repository;

import com.recipes.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Review entity
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByRecipeId(Long recipeId, Pageable pageable);

    Page<Review> findByUserId(Long userId, Pageable pageable);

    Optional<Review> findByUserIdAndRecipeId(Long userId, Long recipeId);

    Boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.recipe.id = :recipeId")
    Double getAverageRatingByRecipeId(Long recipeId);

    Long countByRecipeId(Long recipeId);
}
