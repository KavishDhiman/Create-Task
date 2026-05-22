package com.createtask.createtask.dto.response;

import java.time.LocalDate;

// DTO to send task details back to the client — avoids exposing raw entity relationships
public class TaskResponseDTO {

    // Unique identifier of the task
    private int taskID;

    // Name/title of the task
    private String taskName;

    // Detailed description of the task
    private String description;

    // Deadline for task completion
    private LocalDate dueDate;

    // Priority: High / Medium / Low
    private String priority;

    // Status: Pending / In Progress / Completed
    private String status;

    // ID of the project this task belongs to
    private Integer projectID;

    // Project name — included for display without a second API call
    private String projectName;

    // ID of the user assigned to this task
    private Integer userID;

    // Full name of the assigned user — included for display convenience
    private String userName;

    // Stores success or informational messages for UI responses
    private String message;

    // Default constructor required by Jackson for JSON serialization
    public TaskResponseDTO() {}

    // Returns the task's unique ID
    public int getTaskID() { return taskID; }
    // Sets the task ID
    public void setTaskID(int taskID) { this.taskID = taskID; }

    // Returns the task name
    public String getTaskName() { return taskName; }
    // Sets the task name
    public void setTaskName(String taskName) { this.taskName = taskName; }

    // Returns the description
    public String getDescription() { return description; }
    // Sets the description
    public void setDescription(String description) { this.description = description; }

    // Returns the task due date
    public LocalDate getDueDate() { return dueDate; }
    // Sets the due date
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // Returns the priority string
    public String getPriority() { return priority; }
    // Sets the priority
    public void setPriority(String priority) { this.priority = priority; }

    // Returns the current status
    public String getStatus() { return status; }
    // Sets the status
    public void setStatus(String status) { this.status = status; }

    // Returns the project ID linked to this task
    public Integer getProjectID() { return projectID; }
    // Sets the project ID
    public void setProjectID(Integer projectID) { this.projectID = projectID; }

    // Returns the project name for display purposes
    public String getProjectName() { return projectName; }
    // Sets the project name
    public void setProjectName(String projectName) { this.projectName = projectName; }

    // Returns the assigned user's ID
    public Integer getUserID() { return userID; }
    // Sets the user ID
    public void setUserID(Integer userID) { this.userID = userID; }

    // Returns the assigned user's full name
    public String getUserName() { return userName; }
    // Sets the user's full name
    public void setUserName(String userName) { this.userName = userName; }

    // Returns the response message
    public String getMessage() { return message; }

    // Sets the response message
    public void setMessage(String message) { this.message = message; }
}