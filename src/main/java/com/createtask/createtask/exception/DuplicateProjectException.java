package com.createtask.createtask.exception;

// Thrown when someone tries to create a project with an ID that already exists.
// Keeps our data clean by preventing accidental overwrites.
public class DuplicateProjectException extends RuntimeException {

    public DuplicateProjectException(String message) {
        super(message);
    }
}