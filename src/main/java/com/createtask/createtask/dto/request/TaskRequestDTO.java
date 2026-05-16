package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// DTO to receive task data from the client during create/update operations
public class TaskRequestDTO {

    // Client must supply the ID manually (no auto-increment in DB)
    @NotNull(message = "Task ID is required")
    private Integer taskID;

    // Task must have a name — blank strings are rejected
    @NotBlank(message = "Task name is required")
    private String taskName;

    // Optional long-form description of what the task involves
    private String description;

    // The deadline by which this task should be completed
    private LocalDate dueDate;

    // Priority level: High / Medium / Low
    private String priority;

    // Current state of the task: Pending / In Progress / Completed
    private String status;

    // Foreign key — the project this task belongs to
    private Integer projectID;

    // Foreign key — the user this task is assigned to
    private Integer userID;

    public TaskRequestDTO() {}

    public Integer getTaskID() { return taskID; }
    public void setTaskID(Integer taskID) { this.taskID = taskID; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProjectID() { return projectID; }
    public void setProjectID(Integer projectID) { this.projectID = projectID; }

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }
}