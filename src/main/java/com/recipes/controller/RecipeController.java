package com.recipes.controller;

import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipeRequest;
import com.recipes.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for recipe management
 */
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Recipe management and browsing")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Get all published recipes")
    public ResponseEntity<Page<RecipeDTO>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<RecipeDTO> recipes = recipeService.getAllPublishedRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        RecipeDTO recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes by keyword")
    public ResponseEntity<Page<RecipeDTO>> searchRecipes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDTO> recipes = recipeService.searchRecipes(keyword, pageable);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get recipes by category")
    public ResponseEntity<Page<RecipeDTO>> getRecipesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDTO> recipes = recipeService.getRecipesByCategory(categoryId, pageable);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/difficulty/{difficulty}")
    @Operation(summary = "Get recipes by difficulty level")
    public ResponseEntity<Page<RecipeDTO>> getRecipesByDifficulty(
            @PathVariable String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDTO> recipes = recipeService.getRecipesByDifficulty(difficulty, pageable);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Get top rated recipes")
    public ResponseEntity<Page<RecipeDTO>> getTopRatedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDTO> recipes = recipeService.getTopRatedRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest recipes")
    public ResponseEntity<Page<RecipeDTO>> getLatestRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDTO> recipes = recipeService.getLatestRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get recipes by author")
    public ResponseEntity<Page<RecipeDTO>> getRecipesByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDTO> recipes = recipeService.getRecipesByAuthor(authorId, pageable);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Create a new recipe")
    public ResponseEntity<RecipeDTO> createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        RecipeDTO createdRecipe = recipeService.createRecipe(recipeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Update an existing recipe")
    public ResponseEntity<RecipeDTO> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest recipeRequest) {
        RecipeDTO updatedRecipe = recipeService.updateRecipe(id, recipeRequest);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Delete a recipe")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favorite")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Add recipe to favorites")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long id) {
        recipeService.addToFavorites(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorite")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Remove recipe from favorites")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long id) {
        recipeService.removeFromFavorites(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Get user's favorite recipes")
    public ResponseEntity<Page<RecipeDTO>> getFavoriteRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeDTO> recipes = recipeService.getFavoriteRecipes(pageable);
        return ResponseEntity.ok(recipes);
    }
}
