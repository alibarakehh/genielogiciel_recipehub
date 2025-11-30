package com.recipes.constants;

/**
 * Application-wide constants
 * Centralized constant definitions for better maintainability
 */
public final class AppConstants {
    
    private AppConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // Pagination defaults
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
    
    // JWT Configuration
    public static final long JWT_EXPIRATION_MS = 86400000L; // 24 hours
    public static final String JWT_SECRET_KEY = "jwt.secret";
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    
    // User defaults
    public static final String DEFAULT_PROFILE_IMAGE_BASE = "https://ui-avatars.com/api/?name=";
    public static final String DEFAULT_PROFILE_IMAGE_PARAMS = "&size=200&background=random&color=fff";
    
    // Validation constraints
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MAX_RECIPE_TITLE_LENGTH = 200;
    public static final int MAX_CATEGORY_NAME_LENGTH = 50;
    public static final int MIN_REVIEW_RATING = 1;
    public static final int MAX_REVIEW_RATING = 5;
    
    // Error messages
    public static final String RESOURCE_NOT_FOUND = "%s not found with %s: %s";
    public static final String RESOURCE_ALREADY_EXISTS = "%s already exists with %s: %s";
    public static final String UNAUTHORIZED_ACTION = "You are not authorized to perform this action";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    
    // Recipe constants
    public static final int MIN_PREP_TIME = 0;
    public static final int MIN_COOK_TIME = 0;
    public static final int MIN_SERVINGS = 1;
    public static final int MAX_SERVINGS = 100;
}
