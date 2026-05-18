package com.createtask.createtask.exception;

// Thrown when a client tries to create something that already exists — maps to HTTP 409
public class DuplicateResourceException extends RuntimeException {

    // Accepts a descriptive message so the client understands what caused the conflict
    public DuplicateResourceException(String message) {
        super(message);
    }
}