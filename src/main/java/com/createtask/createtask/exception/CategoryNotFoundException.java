package com.createtask.createtask.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(int categoryId) {
        super("Category not found with ID: " + categoryId);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}