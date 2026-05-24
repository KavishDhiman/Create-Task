package com.createtask.createtask.exception;

// Custom exception for user not found cases
public class UserNotFoundException extends RuntimeException {

    private final Integer userId; // Stores missing user ID

    // Constructor for initializing exception
    public UserNotFoundException(Integer userId) {
        super("User not found with ID: " + userId); // Passes error message to parent exception class
        this.userId = userId; // Assigns user ID value
    }

    // Getter method for userId
    public Integer getUserId() {
        return userId; // Returns missing user ID
    }
}