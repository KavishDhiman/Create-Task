package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.response.CategoryResponseDTO;
import com.createtask.createtask.entity.Category;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.TaskCategory;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.exception.ResourceNotFoundException;
import com.createtask.createtask.repository.CategoryRepository;
import com.createtask.createtask.repository.TaskCategoryRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.service.TaskCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Business logic for managing the many-to-many mapping between Task and Category
@Service
@Transactional
public class TaskCategoryServiceImpl implements TaskCategoryService {

    // Used to verify task existence before mapping
    private final TaskRepository taskRepository;

    // Used to verify category existence before mapping
    private final CategoryRepository categoryRepository;

    // Handles all TaskCategory mapping DB operations
    private final TaskCategoryRepository taskCategoryRepository;

    // Constructor injection for all three repositories
    public TaskCategoryServiceImpl(TaskRepository taskRepository,
                                   CategoryRepository categoryRepository,
                                   TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    // Validates task and category exist, checks for duplicate mapping, then inserts the row
    @Override
    public String assignCategoryToTask(int taskID, int categoryID) {
        // Verify the task exists before attempting to link a category
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskID));

        // Verify the category exists before linking
        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryID));

        // Prevent inserting a duplicate mapping row
        if (taskCategoryRepository.existsByTaskIDAndCategoryID(taskID, categoryID)) {
            throw new DuplicateResourceException(
                    "Category " + categoryID + " is already assigned to Task " + taskID);
        }

        // Build the composite key and entity, then persist
        TaskCategory.TaskCategoryId compositeKey = new TaskCategory.TaskCategoryId();
        compositeKey.setTaskID(taskID);
        compositeKey.setCategoryID(categoryID);

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setId(compositeKey);
        taskCategory.setTask(task);
        taskCategory.setCategory(category);

        taskCategoryRepository.save(taskCategory);
        return "Category '" + category.getCategoryName() + "' successfully assigned to Task " + taskID;
    }

    // Looks up the exact mapping row by composite key, deletes it, returns confirmation
    @Override
    public String removeCategoryFromTask(int taskID, int categoryID) {
        // Build the composite key to locate the exact mapping
        TaskCategory.TaskCategoryId compositeKey = new TaskCategory.TaskCategoryId();
        compositeKey.setTaskID(taskID);
        compositeKey.setCategoryID(categoryID);

        // Throw 404 if the mapping doesn't exist — avoid silent no-ops
        TaskCategory taskCategory = taskCategoryRepository.findById(compositeKey)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mapping not found: Category " + categoryID + " is not assigned to Task " + taskID));

        taskCategoryRepository.delete(taskCategory);
        return "Category " + categoryID + " successfully removed from Task " + taskID;
    }

    // Verifies the task exists, then fetches and maps all its linked categories
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoriesForTask(int taskID) {
        // Confirm the task exists before querying its categories
        if (!taskRepository.existsById(taskID)) {
            throw new ResourceNotFoundException("Task not found with ID: " + taskID);
        }

        // Fetch all TaskCategory rows for this task and project to category DTOs
        return taskCategoryRepository.findByTaskID(taskID).stream()
                .map(tc -> {
                    CategoryResponseDTO dto = new CategoryResponseDTO();
                    dto.setCategoryID(tc.getCategory().getCategoryID());
                    dto.setCategoryName(tc.getCategory().getCategoryName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}