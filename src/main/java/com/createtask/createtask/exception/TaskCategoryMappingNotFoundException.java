package com.createtask.createtask.exception;

public class TaskCategoryMappingNotFoundException extends RuntimeException {
    public TaskCategoryMappingNotFoundException(int taskId, int categoryId) {
        super("No mapping found between Task ID " + taskId + " and Category ID " + categoryId);
    }

    public TaskCategoryMappingNotFoundException(String message) {
        super(message);
    }
}