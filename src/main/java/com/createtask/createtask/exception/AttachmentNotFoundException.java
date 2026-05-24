package com.createtask.createtask.exception;

/**
 * Custom exception thrown when an attachment resource is not found.
 */
public class AttachmentNotFoundException extends RuntimeException {

    /**
     * Constructs a new AttachmentNotFoundException with the specified message.
     *
     * @param message exception message
     */
    public AttachmentNotFoundException(String message) {
        super(message);
    }
}