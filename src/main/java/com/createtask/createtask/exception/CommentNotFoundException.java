package com.createtask.createtask.exception;

/**
 * Custom exception thrown when a comment resource is not found.
 */
public class CommentNotFoundException extends RuntimeException {

    /**
     * Constructs a new CommentNotFoundException with the specified message.
     *
     * @param message exception message
     */
    public CommentNotFoundException(String message) {
        super(message);
    }
}