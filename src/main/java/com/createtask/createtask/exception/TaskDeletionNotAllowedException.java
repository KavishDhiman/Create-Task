package com.createtask.createtask.exception;

// Thrown when attempting to delete a task that still has category mappings
public class TaskDeletionNotAllowedException extends RuntimeException {

    public TaskDeletionNotAllowedException(String message) {
        super(message);
    }
}