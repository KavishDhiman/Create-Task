package com.createtask.createtask.controller;

import com.createtask.createtask.dto.response.CategoryResponseDTO;
import com.createtask.createtask.service.TaskCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handles task-category mapping operations
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task-Category Mapping", description = "Assign, remove, and fetch categories for a task")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    // Initializes task-category service dependency
    public TaskCategoryController(TaskCategoryService taskCategoryService) {
        this.taskCategoryService = taskCategoryService;
    }

    // Assigns a category to a specific task
    @Operation(
            summary = "Assign category to task",
            description = "Assigns an existing category to a specific task"
    )
    @ApiResponse(responseCode = "200", description = "Category assigned to task successfully")
    @PostMapping("/{taskId}/categories/{categoryId}")
    public ResponseEntity<Void> assignCategoryToTask(@PathVariable int taskId,
                                                     @PathVariable int categoryId) {
        taskCategoryService.assignCategoryToTask(taskId, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Removes a category from a specific task
    @Operation(
            summary = "Remove category from task",
            description = "Removes an assigned category from a specific task"
    )
    @ApiResponse(responseCode = "204", description = "Category removed from task successfully")
    @DeleteMapping("/{taskId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromTask(@PathVariable int taskId,
                                                       @PathVariable int categoryId) {
        taskCategoryService.removeCategoryFromTask(taskId, categoryId);
        return ResponseEntity.noContent().build();
    }

    // Retrieves all categories assigned to a task
    @Operation(
            summary = "Get categories for task",
            description = "Fetches all categories assigned to a specific task"
    )
    @ApiResponse(responseCode = "200", description = "Categories fetched successfully")
    @GetMapping("/{taskId}/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesForTask(@PathVariable int taskId) {
        List<CategoryResponseDTO> categories = taskCategoryService.getCategoriesForTask(taskId);
        return ResponseEntity.ok(categories);
    }
}