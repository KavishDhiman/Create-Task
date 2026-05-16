package com.createtask.createtask.exception;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(Integer notificationId) {
        super("Notification with ID " + notificationId + " was not found.");
    }
}