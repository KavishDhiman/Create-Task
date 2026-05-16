package com.createtask.createtask.exception;

/**
 * Thrown when a create or update operation uses a username or email
 * that already exists in the database.
 * Mapped to HTTP 409 by GlobalExceptionHandler.
 */
public class DuplicateUserException extends RuntimeException {

    private final String field;
    private final String value;

    public DuplicateUserException(String field, String value) {
        super("User already exists with " + field + ": " + value);
        this.field = field;
        this.value = value;
    }

    public String getField() { return field; }
    public String getValue() { return value; }
}