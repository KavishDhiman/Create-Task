package com.createtask.createtask.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(int taskId) {
        super("Task not found with ID: " + taskId);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}