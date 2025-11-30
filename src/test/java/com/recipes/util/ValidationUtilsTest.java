package com.recipes.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ValidationUtils
 * Tests validation logic for various inputs
 */
@DisplayName("ValidationUtils Unit Tests")
class ValidationUtilsTest {
    
    // Username validation tests
    @Test
    @DisplayName("Should validate correct username")
    void isValidUsername_WithValidUsername_ReturnsTrue() {
        assertThat(ValidationUtils.isValidUsername("john_doe")).isTrue();
        assertThat(ValidationUtils.isValidUsername("user123")).isTrue();
        assertThat(ValidationUtils.isValidUsername("chef")).isTrue();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"ab", "", "  "})
    @DisplayName("Should reject too short username")
    void isValidUsername_WithTooShortUsername_ReturnsFalse(String username) {
        assertThat(ValidationUtils.isValidUsername(username)).isFalse();
    }
    
    @Test
    @DisplayName("Should reject too long username")
    void isValidUsername_WithTooLongUsername_ReturnsFalse() {
        String longUsername = "a".repeat(51);
        assertThat(ValidationUtils.isValidUsername(longUsername)).isFalse();
    }
    
    @Test
    @DisplayName("Should reject null username")
    void isValidUsername_WithNull_ReturnsFalse() {
        assertThat(ValidationUtils.isValidUsername(null)).isFalse();
    }
    
    // Password validation tests
    @Test
    @DisplayName("Should validate correct password")
    void isValidPassword_WithValidPassword_ReturnsTrue() {
        assertThat(ValidationUtils.isValidPassword("password123")).isTrue();
        assertThat(ValidationUtils.isValidPassword("123456")).isTrue();
        assertThat(ValidationUtils.isValidPassword("strongP@ssw0rd!")).isTrue();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"12345", "abc", ""})
    @DisplayName("Should reject too short password")
    void isValidPassword_WithTooShortPassword_ReturnsFalse(String password) {
        assertThat(ValidationUtils.isValidPassword(password)).isFalse();
    }
    
    @Test
    @DisplayName("Should reject too long password")
    void isValidPassword_WithTooLongPassword_ReturnsFalse() {
        String longPassword = "a".repeat(101);
        assertThat(ValidationUtils.isValidPassword(longPassword)).isFalse();
    }
    
    @Test
    @DisplayName("Should reject null password")
    void isValidPassword_WithNull_ReturnsFalse() {
        assertThat(ValidationUtils.isValidPassword(null)).isFalse();
    }
    
    // Email validation tests
    @Test
    @DisplayName("Should validate correct email")
    void isValidEmail_WithValidEmail_ReturnsTrue() {
        assertThat(ValidationUtils.isValidEmail("user@example.com")).isTrue();
        assertThat(ValidationUtils.isValidEmail("test.user@domain.co.uk")).isTrue();
        assertThat(ValidationUtils.isValidEmail("chef_123@recipes.com")).isTrue();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "@example.com",
        "user@",
        "user@.com",
        "user @example.com",
        "user@example",
        ""
    })
    @DisplayName("Should reject invalid email")
    void isValidEmail_WithInvalidEmail_ReturnsFalse(String email) {
        assertThat(ValidationUtils.isValidEmail(email)).isFalse();
    }
    
    @Test
    @DisplayName("Should reject null email")
    void isValidEmail_WithNull_ReturnsFalse() {
        assertThat(ValidationUtils.isValidEmail(null)).isFalse();
    }
    
    // Rating validation tests
    @Test
    @DisplayName("Should validate correct ratings")
    void isValidRating_WithValidRating_ReturnsTrue() {
        assertThat(ValidationUtils.isValidRating(1)).isTrue();
        assertThat(ValidationUtils.isValidRating(3)).isTrue();
        assertThat(ValidationUtils.isValidRating(5)).isTrue();
    }
    
    @ParameterizedTest
    @ValueSource(ints = {0, -1, 6, 10, 100})
    @DisplayName("Should reject out of range ratings")
    void isValidRating_WithInvalidRating_ReturnsFalse(int rating) {
        assertThat(ValidationUtils.isValidRating(rating)).isFalse();
    }
    
    @Test
    @DisplayName("Should reject null rating")
    void isValidRating_WithNull_ReturnsFalse() {
        assertThat(ValidationUtils.isValidRating(null)).isFalse();
    }
    
    // Sanitize tests
    @Test
    @DisplayName("Should sanitize string by trimming")
    void sanitize_WithWhitespace_TrimsString() {
        assertThat(ValidationUtils.sanitize("  test  ")).isEqualTo("test");
        assertThat(ValidationUtils.sanitize("\nvalue\t")).isEqualTo("value");
    }
    
    @Test
    @DisplayName("Should return null for null input")
    void sanitize_WithNull_ReturnsNull() {
        assertThat(ValidationUtils.sanitize(null)).isNull();
    }
    
    @Test
    @DisplayName("Should handle empty string")
    void sanitize_WithEmptyString_ReturnsEmpty() {
        assertThat(ValidationUtils.sanitize("")).isEmpty();
        assertThat(ValidationUtils.sanitize("   ")).isEmpty();
    }
}
