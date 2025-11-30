package com.recipes.mapper;

import com.recipes.dto.CategoryDTO;
import com.recipes.model.Category;
import org.springframework.stereotype.Component;

/**
 * Mapper for Category entity and DTO conversions
 * Following Single Responsibility Principle
 */
@Component
public class CategoryMapper {
    
    /**
     * Convert Category entity to DTO
     * @param category The category entity
     * @return CategoryDTO
     */
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setIconUrl(category.getIconUrl());
        return dto;
    }
    
    /**
     * Convert CategoryDTO to entity
     * @param dto The category DTO
     * @return Category entity
     */
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIconUrl(dto.getIconUrl());
        return category;
    }
    
    /**
     * Update existing entity with DTO data
     * @param dto The source DTO
     * @param category The target entity
     */
    public void updateEntityFromDTO(CategoryDTO dto, Category category) {
        if (dto == null || category == null) {
            return;
        }
        
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIconUrl(dto.getIconUrl());
    }
}
