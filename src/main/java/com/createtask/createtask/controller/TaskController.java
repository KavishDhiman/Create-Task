package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.TaskRequestDTO;
import com.createtask.createtask.dto.response.TaskResponseDTO;
import com.createtask.createtask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller handling all Task endpoints — base path /api/v1
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Tasks", description = "Create, read, update, delete and filter tasks")
public class TaskController {

    // Service layer that handles task business logic
    private final TaskService taskService;

    // Constructor injection for TaskService
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ── TASK CRUD ──────────────────────────────────────────────────────────────

    // Creates a new task and returns it with HTTP 201
    @Operation(summary = "Create a new task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Referenced project or user not found")
    })
    @PostMapping("/tasks")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(requestDTO));
    }

    // Fetches a single task by its ID — returns 404 if not found
    @Operation(summary = "Get task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @Parameter(description = "ID of the task to retrieve") @PathVariable int taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    // Returns all tasks in the system
    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "List of all tasks")
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Updates task fields — returns 404 if the task doesn't exist
    @Operation(summary = "Update an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @Parameter(description = "ID of the task to update") @PathVariable int taskId,
            @Valid @RequestBody TaskRequestDTO requestDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, requestDTO));
    }

    // Deletes a task — returns 204 No Content on success
    @Operation(summary = "Delete a task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete") @PathVariable int taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    // ── FILTERING ──────────────────────────────────────────────────────────────

    // Filters tasks by status value (e.g., Pending, In Progress, Completed)
    @Operation(summary = "Filter tasks by status", description = "Accepted values: Pending, In Progress, Completed")
    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(
            @Parameter(description = "Status to filter by") @PathVariable String status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    // Filters tasks by priority value (e.g., High, Medium, Low)
    @Operation(summary = "Filter tasks by priority", description = "Accepted values: High, Medium, Low")
    @GetMapping("/tasks/priority/{priority}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByPriority(
            @Parameter(description = "Priority to filter by") @PathVariable String priority) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority));
    }

    // ── NESTED / ASSIGNMENT ENDPOINTS ─────────────────────────────────────────

    // Returns all tasks under a specific project — returns 404 if project not found
    @Operation(summary = "Get all tasks under a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByProject(
            @Parameter(description = "ID of the project") @PathVariable int projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    // Returns all tasks assigned to a specific user — returns 404 if user not found
    @Operation(summary = "Get all tasks assigned to a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser(
            @Parameter(description = "ID of the user") @PathVariable int userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }
}