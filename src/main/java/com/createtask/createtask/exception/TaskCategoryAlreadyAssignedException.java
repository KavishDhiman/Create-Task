package com.createtask.createtask.exception;

public class TaskCategoryAlreadyAssignedException extends RuntimeException {
    public TaskCategoryAlreadyAssignedException(int taskId, int categoryId) {
        super("Category ID " + categoryId + " is already assigned to Task ID " + taskId);
    }

    public TaskCategoryAlreadyAssignedException(String message) {
        super(message);
    }
}