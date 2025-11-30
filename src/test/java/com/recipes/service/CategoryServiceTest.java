package com.recipes.service;

import com.recipes.dto.CategoryDTO;
import com.recipes.exception.ResourceAlreadyExistsException;
import com.recipes.exception.ResourceNotFoundException;
import com.recipes.mapper.CategoryMapper;
import com.recipes.model.Category;
import com.recipes.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CategoryService
 * Tests business logic in isolation using mocks
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Unit Tests")
class CategoryServiceTest {
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private CategoryMapper categoryMapper;
    
    @InjectMocks
    private CategoryService categoryService;
    
    private Category italianCategory;
    private Category mexicanCategory;
    private CategoryDTO italianDTO;
    private CategoryDTO mexicanDTO;
    
    @BeforeEach
    void setUp() {
        // Setup test data
        italianCategory = createCategory(1L, "Italian", "Traditional Italian cuisine");
        mexicanCategory = createCategory(2L, "Mexican", "Spicy Mexican dishes");
        
        italianDTO = createCategoryDTO(1L, "Italian", "Traditional Italian cuisine");
        mexicanDTO = createCategoryDTO(2L, "Mexican", "Spicy Mexican dishes");
    }
    
    @Test
    @DisplayName("Should return all categories when getAllCategories is called")
    void getAllCategories_ReturnsAllCategories() {
        // Given
        List<Category> categories = Arrays.asList(italianCategory, mexicanCategory);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDTO(italianCategory)).thenReturn(italianDTO);
        when(categoryMapper.toDTO(mexicanCategory)).thenReturn(mexicanDTO);
        
        // When
        List<CategoryDTO> result = categoryService.getAllCategories();
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").containsExactly("Italian", "Mexican");
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(2)).toDTO(any(Category.class));
    }
    
    @Test
    @DisplayName("Should return category when getCategoryById is called with existing ID")
    void getCategoryById_WithExistingId_ReturnsCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(italianCategory));
        when(categoryMapper.toDTO(italianCategory)).thenReturn(italianDTO);
        
        // When
        CategoryDTO result = categoryService.getCategoryById(1L);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Italian");
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDTO(italianCategory);
    }
    
    @Test
    @DisplayName("Should throw ResourceNotFoundException when getCategoryById is called with non-existing ID")
    void getCategoryById_WithNonExistingId_ThrowsException() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When/Then
        assertThatThrownBy(() -> categoryService.getCategoryById(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Category")
            .hasMessageContaining("999");
        
        verify(categoryRepository).findById(999L);
        verify(categoryMapper, never()).toDTO(any());
    }
    
    @Test
    @DisplayName("Should create category when createCategory is called with valid data")
    void createCategory_WithValidData_CreatesAndReturnsCategory() {
        // Given
        CategoryDTO newCategoryDTO = createCategoryDTO(null, "Asian", "Asian fusion cuisine");
        Category newCategory = createCategory(null, "Asian", "Asian fusion cuisine");
        Category savedCategory = createCategory(3L, "Asian", "Asian fusion cuisine");
        CategoryDTO savedDTO = createCategoryDTO(3L, "Asian", "Asian fusion cuisine");
        
        when(categoryRepository.existsByName("Asian")).thenReturn(false);
        when(categoryMapper.toEntity(newCategoryDTO)).thenReturn(newCategory);
        when(categoryRepository.save(newCategory)).thenReturn(savedCategory);
        when(categoryMapper.toDTO(savedCategory)).thenReturn(savedDTO);
        
        // When
        CategoryDTO result = categoryService.createCategory(newCategoryDTO);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Asian");
        verify(categoryRepository).existsByName("Asian");
        verify(categoryRepository).save(newCategory);
    }
    
    @Test
    @DisplayName("Should throw ResourceAlreadyExistsException when creating duplicate category")
    void createCategory_WithDuplicateName_ThrowsException() {
        // Given
        CategoryDTO duplicateDTO = createCategoryDTO(null, "Italian", "Another Italian");
        when(categoryRepository.existsByName("Italian")).thenReturn(true);
        
        // When/Then
        assertThatThrownBy(() -> categoryService.createCategory(duplicateDTO))
            .isInstanceOf(ResourceAlreadyExistsException.class)
            .hasMessageContaining("Italian");
        
        verify(categoryRepository).existsByName("Italian");
        verify(categoryRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should update category when updateCategory is called with valid data")
    void updateCategory_WithValidData_UpdatesAndReturnsCategory() {
        // Given
        CategoryDTO updateDTO = createCategoryDTO(1L, "Italian Cuisine", "Updated description");
        Category existingCategory = createCategory(1L, "Italian", "Old description");
        Category updatedCategory = createCategory(1L, "Italian Cuisine", "Updated description");
        CategoryDTO updatedDTO = createCategoryDTO(1L, "Italian Cuisine", "Updated description");
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Italian Cuisine")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDTO(updatedCategory)).thenReturn(updatedDTO);
        
        // When
        CategoryDTO result = categoryService.updateCategory(1L, updateDTO);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Italian Cuisine");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }
    
    @Test
    @DisplayName("Should delete category when deleteCategory is called with existing ID")
    void deleteCategory_WithExistingId_DeletesCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(italianCategory));
        doNothing().when(categoryRepository).delete(italianCategory);
        
        // When
        categoryService.deleteCategory(1L);
        
        // Then
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(italianCategory);
    }
    
    @Test
    @DisplayName("Should throw exception when deleting non-existing category")
    void deleteCategory_WithNonExistingId_ThrowsException() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When/Then
        assertThatThrownBy(() -> categoryService.deleteCategory(999L))
            .isInstanceOf(ResourceNotFoundException.class);
        
        verify(categoryRepository).findById(999L);
        verify(categoryRepository, never()).delete(any());
    }
    
    // Helper methods
    private Category createCategory(Long id, String name, String description) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setIconUrl("https://example.com/icon.jpg");
        return category;
    }
    
    private CategoryDTO createCategoryDTO(Long id, String name, String description) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setIconUrl("https://example.com/icon.jpg");
        return dto;
    }
}
