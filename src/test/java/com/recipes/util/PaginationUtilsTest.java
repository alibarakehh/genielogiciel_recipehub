package com.recipes.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.recipes.constants.AppConstants.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for PaginationUtils
 * Tests pagination logic and validation
 */
@DisplayName("PaginationUtils Unit Tests")
class PaginationUtilsTest {
    
    @Test
    @DisplayName("Should create pageable with default values")
    void createPageable_WithDefaults_ReturnsValidPageable() {
        // When
        Pageable result = PaginationUtils.createPageable(0, 10);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPageNumber()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(10);
    }
    
    @Test
    @DisplayName("Should handle negative page number by setting to 0")
    void createPageable_WithNegativePage_SetsToZero() {
        // When
        Pageable result = PaginationUtils.createPageable(-5, 10);
        
        // Then
        assertThat(result.getPageNumber()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Should handle zero page size by setting to 1")
    void createPageable_WithZeroSize_SetsToOne() {
        // When
        Pageable result = PaginationUtils.createPageable(0, 0);
        
        // Then
        assertThat(result.getPageSize()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Should cap page size at maximum")
    void createPageable_WithOversizedPage_CapsAtMax() {
        // When
        Pageable result = PaginationUtils.createPageable(0, 500);
        
        // Then
        assertThat(result.getPageSize()).isEqualTo(MAX_PAGE_SIZE);
    }
    
    @Test
    @DisplayName("Should create pageable with sorting")
    void createPageable_WithSorting_ReturnsPageableWithSort() {
        // When
        Pageable result = PaginationUtils.createPageable(0, 10, "name", "ASC");
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPageNumber()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(10);
        assertThat(result.getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, "name"));
    }
    
    @Test
    @DisplayName("Should handle invalid sort direction")
    void createPageable_WithInvalidDirection_UsesDefault() {
        // When
        Pageable result = PaginationUtils.createPageable(0, 10, "name", "INVALID");
        
        // Then
        assertThat(result.getSort()).isEqualTo(
            Sort.by(Sort.Direction.fromString(DEFAULT_SORT_DIRECTION), "name")
        );
    }
    
    @Test
    @DisplayName("Should handle null sort field")
    void createPageable_WithNullSortBy_UsesDefault() {
        // When
        Pageable result = PaginationUtils.createPageable(0, 10, null, "DESC");
        
        // Then
        assertThat(result.getSort()).isEqualTo(
            Sort.by(Sort.Direction.DESC, DEFAULT_SORT_BY)
        );
    }
    
    @Test
    @DisplayName("Should handle blank sort field")
    void createPageable_WithBlankSortBy_UsesDefault() {
        // When
        Pageable result = PaginationUtils.createPageable(0, 10, "   ", "DESC");
        
        // Then
        assertThat(result.getSort()).isEqualTo(
            Sort.by(Sort.Direction.DESC, DEFAULT_SORT_BY)
        );
    }
}
