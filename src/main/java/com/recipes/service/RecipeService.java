package com.recipes.service;

import com.recipes.dto.IngredientItemDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipeRequest;
import com.recipes.exception.ResourceNotFoundException;
import com.recipes.exception.UnauthorizedException;
import com.recipes.model.*;
import com.recipes.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for recipe management operations
 */
@Service
@RequiredArgsConstructor
public class RecipeService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public Page<RecipeDTO> getAllPublishedRecipes(Pageable pageable) {
        return recipeRepository.findByPublishedTrue(pageable)
                .map(this::convertToDTO);
    }

    public RecipeDTO getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
        
        // Increment view count
        recipe.setViewCount(recipe.getViewCount() + 1);
        recipeRepository.save(recipe);
        
        return convertToDTO(recipe);
    }

    public Page<RecipeDTO> searchRecipes(String keyword, Pageable pageable) {
        return recipeRepository.searchByKeyword(keyword, pageable)
                .map(this::convertToDTO);
    }

    public Page<RecipeDTO> getRecipesByCategory(Long categoryId, Pageable pageable) {
        return recipeRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToDTO);
    }

    public Page<RecipeDTO> getRecipesByDifficulty(String difficulty, Pageable pageable) {
        Recipe.Difficulty diff = Recipe.Difficulty.valueOf(difficulty.toUpperCase());
        return recipeRepository.findByDifficulty(diff, pageable)
                .map(this::convertToDTO);
    }

    public Page<RecipeDTO> getTopRatedRecipes(Pageable pageable) {
        return recipeRepository.findTopRated(pageable)
                .map(this::convertToDTO);
    }

    public Page<RecipeDTO> getLatestRecipes(Pageable pageable) {
        return recipeRepository.findLatest(pageable)
                .map(this::convertToDTO);
    }

    public Page<RecipeDTO> getRecipesByAuthor(Long authorId, Pageable pageable) {
        return recipeRepository.findByAuthorId(authorId, pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public RecipeDTO createRecipe(RecipeRequest recipeRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Recipe recipe = new Recipe();
        mapRequestToEntity(recipeRequest, recipe, author);

        Recipe savedRecipe = recipeRepository.save(recipe);
        
        // Save ingredients
        if (recipeRequest.getIngredients() != null && !recipeRequest.getIngredients().isEmpty()) {
            saveRecipeIngredients(savedRecipe, recipeRequest.getIngredients());
        }

        logger.info("Recipe created: {} by user {}", savedRecipe.getTitle(), author.getUsername());
        return convertToDTO(savedRecipe);
    }

    @Transactional
    public RecipeDTO updateRecipe(Long id, RecipeRequest recipeRequest) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));

        // Check authorization
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!recipe.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("You are not authorized to update this recipe");
        }

        mapRequestToEntity(recipeRequest, recipe, recipe.getAuthor());
        
        // Update ingredients
        recipeIngredientRepository.deleteByRecipeId(id);
        if (recipeRequest.getIngredients() != null && !recipeRequest.getIngredients().isEmpty()) {
            saveRecipeIngredients(recipe, recipeRequest.getIngredients());
        }

        Recipe updatedRecipe = recipeRepository.save(recipe);
        logger.info("Recipe updated: {}", updatedRecipe.getTitle());
        return convertToDTO(updatedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));

        // Check authorization
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!recipe.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("You are not authorized to delete this recipe");
        }

        recipeRepository.delete(recipe);
        logger.info("Recipe deleted: {}", recipe.getTitle());
    }

    @Transactional
    public void addToFavorites(Long recipeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);
    }

    @Transactional
    public void removeFromFavorites(Long recipeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

        user.getFavoriteRecipes().remove(recipe);
        userRepository.save(user);
    }

    public Page<RecipeDTO> getFavoriteRecipes(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Convert Set to Page (simplified version)
        List<RecipeDTO> favorites = user.getFavoriteRecipes().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return Page.empty(pageable);
    }

    private void mapRequestToEntity(RecipeRequest request, Recipe recipe, User author) {
        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());
        recipe.setImageUrl(request.getImageUrl());
        recipe.setPrepTime(request.getPrepTime());
        recipe.setCookTime(request.getCookTime());
        recipe.setServings(request.getServings());
        recipe.setDifficulty(Recipe.Difficulty.valueOf(request.getDifficulty().toUpperCase()));
        recipe.setCalories(request.getCalories());
        recipe.setProtein(request.getProtein());
        recipe.setCarbohydrates(request.getCarbohydrates());
        recipe.setFat(request.getFat());
        recipe.setFiber(request.getFiber());
        recipe.setPublished(request.getPublished());
        recipe.setAuthor(author);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            recipe.setCategory(category);
        }
    }

    private void saveRecipeIngredients(Recipe recipe, List<IngredientItemDTO> ingredientItems) {
        for (int i = 0; i < ingredientItems.size(); i++) {
            IngredientItemDTO item = ingredientItems.get(i);
            
            Ingredient ingredient;
            if (item.getIngredientId() != null) {
                ingredient = ingredientRepository.findById(item.getIngredientId())
                        .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", item.getIngredientId()));
            } else if (item.getIngredientName() != null) {
                ingredient = ingredientRepository.findByName(item.getIngredientName())
                        .orElseGet(() -> {
                            Ingredient newIngredient = new Ingredient();
                            newIngredient.setName(item.getIngredientName());
                            return ingredientRepository.save(newIngredient);
                        });
            } else {
                continue;
            }

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(item.getQuantity());
            recipeIngredient.setUnit(item.getUnit());
            recipeIngredient.setNotes(item.getNotes());
            recipeIngredient.setDisplayOrder(i);

            recipeIngredientRepository.save(recipeIngredient);
        }
    }

    private RecipeDTO convertToDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setTotalTime(recipe.getTotalTime());
        dto.setServings(recipe.getServings());
        dto.setDifficulty(recipe.getDifficulty().name());
        dto.setCalories(recipe.getCalories());
        dto.setProtein(recipe.getProtein());
        dto.setCarbohydrates(recipe.getCarbohydrates());
        dto.setFat(recipe.getFat());
        dto.setFiber(recipe.getFiber());
        dto.setAverageRating(recipe.getAverageRating());
        dto.setReviewCount(recipe.getReviewCount());
        dto.setViewCount(recipe.getViewCount());
        dto.setPublished(recipe.getPublished());
        dto.setAuthorId(recipe.getAuthor().getId());
        dto.setAuthorUsername(recipe.getAuthor().getUsername());
        
        if (recipe.getCategory() != null) {
            dto.setCategoryId(recipe.getCategory().getId());
            dto.setCategoryName(recipe.getCategory().getName());
        }
        
        List<IngredientItemDTO> ingredients = recipeIngredientRepository.findByRecipeIdOrderByDisplayOrder(recipe.getId())
                .stream()
                .map(ri -> new IngredientItemDTO(
                        ri.getIngredient().getId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity(),
                        ri.getUnit(),
                        ri.getNotes(),
                        ri.getDisplayOrder()
                ))
                .collect(Collectors.toList());
        dto.setIngredients(ingredients);
        
        dto.setCreatedAt(recipe.getCreatedAt());
        dto.setUpdatedAt(recipe.getUpdatedAt());
        
        return dto;
    }
}
