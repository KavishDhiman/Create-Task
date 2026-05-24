package com.createtask.createtask.exception;

// Custom exception for duplicate user cases
public class DuplicateUserException extends RuntimeException {

    private final String field; // Stores duplicate field name
    private final String value; // Stores duplicate field value

    // Constructor for initializing exception
    public DuplicateUserException(String field, String value) {
        super("User already exists with " + field + ": " + value); // Passes error message to parent exception class
        this.field = field; // Assigns duplicate field name
        this.value = value; // Assigns duplicate field value
    }

    // Getter method for field
    public String getField() {
        return field; // Returns duplicate field name
    }

    // Getter method for value
    public String getValue() {
        return value; // Returns duplicate field value
    }
}