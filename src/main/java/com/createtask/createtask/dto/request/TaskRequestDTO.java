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

    // Default constructor required by Jackson for JSON deserialization
    public TaskRequestDTO() {}

    // Returns the manually assigned task ID
    public Integer getTaskID() { return taskID; }
    // Sets the task ID provided by the client
    public void setTaskID(Integer taskID) { this.taskID = taskID; }

    // Returns the task's name
    public String getTaskName() { return taskName; }
    // Sets the task name — must not be blank
    public void setTaskName(String taskName) { this.taskName = taskName; }

    // Returns the optional description
    public String getDescription() { return description; }
    // Sets the task description
    public void setDescription(String description) { this.description = description; }

    // Returns the due date for this task
    public LocalDate getDueDate() { return dueDate; }
    // Sets the due date
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // Returns the priority string (High / Medium / Low)
    public String getPriority() { return priority; }
    // Sets the priority
    public void setPriority(String priority) { this.priority = priority; }

    // Returns the current status string
    public String getStatus() { return status; }
    // Sets the status (Pending / In Progress / Completed)
    public void setStatus(String status) { this.status = status; }

    // Returns the project ID this task is linked to
    public Integer getProjectID() { return projectID; }
    // Sets the project FK
    public void setProjectID(Integer projectID) { this.projectID = projectID; }

    // Returns the user ID this task is assigned to
    public Integer getUserID() { return userID; }
    // Sets the user FK
    public void setUserID(Integer userID) { this.userID = userID; }
}