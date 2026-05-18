package com.createtask.createtask.controller;

import com.createtask.createtask.dto.response.CategoryResponseDTO;
import com.createtask.createtask.service.TaskCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task-Category Mapping", description = "Assign, remove, and fetch categories for a task")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    public TaskCategoryController(TaskCategoryService taskCategoryService) {
        this.taskCategoryService = taskCategoryService;
    }

    // Assigns a category to a specific task
    @PostMapping("/{taskId}/categories/{categoryId}")
    public ResponseEntity<Void> assignCategoryToTask(@PathVariable int taskId,
                                                     @PathVariable int categoryId) {
        taskCategoryService.assignCategoryToTask(taskId, categoryId);
        return ResponseEntity.ok().build();
    }

    // Removes a category from a specific task
    @DeleteMapping("/{taskId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromTask(@PathVariable int taskId,
                                                       @PathVariable int categoryId) {
        taskCategoryService.removeCategoryFromTask(taskId, categoryId);
        return ResponseEntity.noContent().build();
    }

    // Fetches all categories assigned to a specific task
    @GetMapping("/{taskId}/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesForTask(@PathVariable int taskId) {
        List<CategoryResponseDTO> categories = taskCategoryService.getCategoriesForTask(taskId);
        return ResponseEntity.ok(categories);
    }
}
