package com.createtask.createtask.exception;

public class NotificationRecipientNotFoundException extends RuntimeException {

    public NotificationRecipientNotFoundException(String message) {
        super(message);
    }

    public NotificationRecipientNotFoundException(Integer userId) {
        super("User with ID " + userId + " was not found.");
    }
}