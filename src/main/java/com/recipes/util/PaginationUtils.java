package com.recipes.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.recipes.constants.AppConstants.*;

/**
 * Utility class for pagination operations
 * Provides standardized page request creation
 */
public final class PaginationUtils {
    
    private PaginationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Create a pageable object with validation
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return Valid Pageable object
     */
    public static Pageable createPageable(int page, int size) {
        int validPage = Math.max(page, DEFAULT_PAGE_NUMBER);
        int validSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return PageRequest.of(validPage, validSize);
    }
    
    /**
     * Create a pageable object with sorting
     * @param page Page number
     * @param size Page size
     * @param sortBy Field to sort by
     * @param direction Sort direction (ASC/DESC)
     * @return Valid Pageable object with sorting
     */
    public static Pageable createPageable(int page, int size, String sortBy, String direction) {
        int validPage = Math.max(page, DEFAULT_PAGE_NUMBER);
        int validSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.fromString(DEFAULT_SORT_DIRECTION);
        }
        
        String validSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : DEFAULT_SORT_BY;
        return PageRequest.of(validPage, validSize, Sort.by(sortDirection, validSortBy));
    }
}
