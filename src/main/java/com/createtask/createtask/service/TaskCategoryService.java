package com.createtask.createtask.service;

import com.createtask.createtask.dto.response.CategoryResponseDTO;

import java.util.List;

// Contract for task-category mapping operations (many-to-many relationship management)
public interface TaskCategoryService {

    // Assigns a category to a task — returns success message, throws 404/409 on error
    String assignCategoryToTask(int taskID, int categoryID);

    // Removes a category from a task — returns confirmation message, throws 404 if not found
    String removeCategoryFromTask(int taskID, int categoryID);

    // Returns all categories currently linked to a given task — throws 404 if task not found
    List<CategoryResponseDTO> getCategoriesForTask(int taskID);
}