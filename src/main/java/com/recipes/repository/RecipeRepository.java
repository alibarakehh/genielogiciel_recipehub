package com.recipes.repository;

import com.recipes.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Recipe entity
 * 
 * Provides database operations and complex queries for Recipe management
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findByPublishedTrue(Pageable pageable);

    Page<Recipe> findByAuthorId(Long authorId, Pageable pageable);

    Page<Recipe> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.published = true AND " +
           "(LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Recipe> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.published = true AND r.difficulty = :difficulty")
    Page<Recipe> findByDifficulty(@Param("difficulty") Recipe.Difficulty difficulty, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.published = true AND r.prepTime + r.cookTime <= :maxTime")
    Page<Recipe> findByMaxTotalTime(@Param("maxTime") Integer maxTime, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.published = true ORDER BY r.averageRating DESC, r.reviewCount DESC")
    Page<Recipe> findTopRated(Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.published = true ORDER BY r.createdAt DESC")
    Page<Recipe> findLatest(Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.published = true ORDER BY r.viewCount DESC")
    Page<Recipe> findMostViewed(Pageable pageable);

    @Query("SELECT r FROM Recipe r JOIN r.recipeIngredients ri WHERE ri.ingredient.id IN :ingredientIds " +
           "GROUP BY r.id HAVING COUNT(DISTINCT ri.ingredient.id) = :count")
    Page<Recipe> findByIngredients(@Param("ingredientIds") List<Long> ingredientIds, 
                                   @Param("count") Long count, 
                                   Pageable pageable);

    @Query("SELECT COUNT(r) FROM Recipe r WHERE r.author.id = :authorId")
    Long countByAuthorId(@Param("authorId") Long authorId);
}
