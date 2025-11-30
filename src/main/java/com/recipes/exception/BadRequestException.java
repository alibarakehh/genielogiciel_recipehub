package com.recipes.exception;

/**
 * Exception thrown for bad requests or invalid input
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
