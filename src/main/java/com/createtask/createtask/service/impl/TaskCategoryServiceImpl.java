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

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    public TaskCategoryServiceImpl(TaskRepository taskRepository,
                                   CategoryRepository categoryRepository,
                                   TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    @Override
    public void assignCategoryToTask(int taskID, int categoryID) {
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskID));

        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryID));

        if (taskCategoryRepository.existsByTaskIDAndCategoryID(taskID, categoryID)) {
            throw new DuplicateResourceException(
                    "Category " + categoryID + " is already assigned to Task " + taskID);
        }

        TaskCategory.TaskCategoryId compositeKey = new TaskCategory.TaskCategoryId();
        compositeKey.setTaskID(taskID);
        compositeKey.setCategoryID(categoryID);

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setId(compositeKey);
        taskCategory.setTask(task);
        taskCategory.setCategory(category);

        taskCategoryRepository.save(taskCategory);
    }

    @Override
    public void removeCategoryFromTask(int taskID, int categoryID) {
        TaskCategory.TaskCategoryId compositeKey = new TaskCategory.TaskCategoryId();
        compositeKey.setTaskID(taskID);
        compositeKey.setCategoryID(categoryID);

        TaskCategory taskCategory = taskCategoryRepository.findById(compositeKey)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mapping not found: Category " + categoryID + " is not assigned to Task " + taskID));

        taskCategoryRepository.delete(taskCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getCategoriesForTask(int taskID) {
        if (!taskRepository.existsById(taskID)) {
            throw new ResourceNotFoundException("Task not found with ID: " + taskID);
        }

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