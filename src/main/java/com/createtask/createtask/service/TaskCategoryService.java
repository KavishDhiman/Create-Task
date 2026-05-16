package com.createtask.createtask.service;

import com.createtask.createtask.dto.response.CategoryResponseDTO;

import java.util.List;

// Contract for task-category mapping operations (many-to-many relationship management)
public interface TaskCategoryService {

    // Assigns a category to a task — throws 404 if either doesn't exist, 409 if already linked
    void assignCategoryToTask(int taskID, int categoryID);

    // Removes a category from a task — throws 404 if the mapping doesn't exist
    void removeCategoryFromTask(int taskID, int categoryID);

    // Returns all categories currently linked to a given task — throws 404 if task not found
    List<CategoryResponseDTO> getCategoriesForTask(int taskID);
}