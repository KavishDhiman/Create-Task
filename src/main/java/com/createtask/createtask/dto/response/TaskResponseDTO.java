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

    public TaskResponseDTO() {}

    public int getTaskID() { return taskID; }
    public void setTaskID(int taskID) { this.taskID = taskID; }

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

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}