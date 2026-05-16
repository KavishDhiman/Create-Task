package com.createtask.createtask.exception;

/**
 * Thrown when a user lookup by ID finds no matching record.
 * Mapped to HTTP 404 by GlobalExceptionHandler.
 */
public class UserNotFoundException extends RuntimeException {

    private final Integer userId;

    public UserNotFoundException(Integer userId) {
        super("User not found with ID: " + userId);
        this.userId = userId;
    }

    public Integer getUserId() { return userId; }
}