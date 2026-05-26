package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a Task.
 * Validation stays here so invalid input is rejected at the API boundary.
 */
public class TaskRequestDTO {

    // Task ID entered by the client
    @NotNull(message = "Task ID is required")
    @Positive(message = "Task ID must be a positive number")
    private Integer taskID;

    // Task title/name
    @NotBlank(message = "Task name is required")
    @Size(min = 3, max = 255, message = "Task name must be between 3 and 255 characters")
    private String taskName;

    // Task description
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
    private String description;

    // Task due date
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    // Priority level
    @NotBlank(message = "Priority is required")
    @Pattern(
            regexp = "High|Medium|Low",
            message = "Priority must be one of: High, Medium, Low"
    )
    private String priority;

    // Task status
    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "Pending|In Progress|Completed",
            message = "Status must be one of: Pending, In Progress, Completed"
    )
    private String status;

    // Project ID linked to the task
    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be a positive number")
    private Integer projectID;

    // User ID linked to the task
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Integer userID;

    public TaskRequestDTO() {
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}