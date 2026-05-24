package com.createtask.createtask.exception;

// Custom exception thrown when the recipient user for a notification does not exist in the database

public class NotificationRecipientNotFoundException extends RuntimeException {
    // Constructor used for passing a custom exception message
    public NotificationRecipientNotFoundException(String message) {

        // Passes the custom message to the parent RuntimeException class
        super(message);
    }
    // Constructor used when a user lookup fails using user ID
    public NotificationRecipientNotFoundException(Integer userId) {

        // Creates a clear and readable error message
        super("User with ID " + userId + " was not found.");
    }
}