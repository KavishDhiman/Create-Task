package com.createtask.createtask.exception;

// Custom exception thrown when a requested notification does not exist in the database
public class NotificationNotFoundException extends RuntimeException {

    // Constructor used for passing a custom exception message
    public NotificationNotFoundException(String message) {

        // Passes the custom message to the parent RuntimeException class
        super(message);
    }

    // Constructor used when notification lookup fails using notification ID
    public NotificationNotFoundException(Integer notificationId) {

        // Creates a clear and user-friendly error message
        super("Notification with ID " + notificationId + " was not found.");
    }
}