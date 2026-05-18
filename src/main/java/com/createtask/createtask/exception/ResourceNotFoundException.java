package com.createtask.createtask.exception;

// Thrown when a requested resource (Task, Category, etc.) cannot be found — maps to HTTP 404
public class ResourceNotFoundException extends RuntimeException {

    // Accepts a descriptive message so the client knows exactly what was not found
    public ResourceNotFoundException(String message) {
        super(message);
    }
}