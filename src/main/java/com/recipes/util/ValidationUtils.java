package com.recipes.util;

import com.recipes.constants.AppConstants;

/**
 * Utility class for validation operations
 * Centralized validation logic
 */
public final class ValidationUtils {
    
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Validate username meets requirements
     * @param username The username to validate
     * @return true if valid
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        int length = username.trim().length();
        return length >= AppConstants.MIN_USERNAME_LENGTH && 
               length <= AppConstants.MAX_USERNAME_LENGTH;
    }
    
    /**
     * Validate password strength
     * @param password The password to validate
     * @return true if valid
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        int length = password.length();
        return length >= AppConstants.MIN_PASSWORD_LENGTH && 
               length <= AppConstants.MAX_PASSWORD_LENGTH;
    }
    
    /**
     * Validate email format
     * @param email The email to validate
     * @return true if valid format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        // Basic email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Validate recipe rating
     * @param rating The rating value
     * @return true if valid (1-5)
     */
    public static boolean isValidRating(Integer rating) {
        if (rating == null) {
            return false;
        }
        return rating >= AppConstants.MIN_REVIEW_RATING && 
               rating <= AppConstants.MAX_REVIEW_RATING;
    }
    
    /**
     * Sanitize string input
     * @param input The input string
     * @return Sanitized string or null
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input.trim();
    }
}
