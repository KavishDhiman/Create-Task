package com.createtask.createtask.exception;

// Thrown when a client tries to create something that already exists — maps to HTTP 409
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}