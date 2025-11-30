package com.recipes.repository;

import com.recipes.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for RecipeIngredient entity
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    List<RecipeIngredient> findByRecipeIdOrderByDisplayOrder(Long recipeId);

    void deleteByRecipeId(Long recipeId);
}
