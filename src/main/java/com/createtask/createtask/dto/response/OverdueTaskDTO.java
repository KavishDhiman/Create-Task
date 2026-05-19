package com.createtask.createtask.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * Response DTO for the overdue tasks report.
 */
public class OverdueTaskDTO {

    private Integer taskId;
    private String taskName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private String assignedUser;
    private String projectName;
    private int daysOverdue;

    // Creates an overdue task report response.
    public OverdueTaskDTO() {
    }

    // Creates an overdue task report response with all fields.
    public OverdueTaskDTO(Integer taskId, String taskName, LocalDate dueDate,
                          String assignedUser, String projectName, int daysOverdue) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.assignedUser = assignedUser;
        this.projectName = projectName;
        this.daysOverdue = daysOverdue;
    }

    // Returns the task ID.
    public Integer getTaskId() {
        return taskId;
    }

    // Sets the task ID.
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    // Returns the task name.
    public String getTaskName() {
        return taskName;
    }

    // Sets the task name.
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Returns the due date.
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Sets the due date.
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Returns the assigned user name.
    public String getAssignedUser() {
        return assignedUser;
    }

    // Sets the assigned user name.
    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    // Returns the project name.
    public String getProjectName() {
        return projectName;
    }

    // Sets the project name.
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // Returns the overdue days count.
    public int getDaysOverdue() {
        return daysOverdue;
    }

    // Sets the overdue days count.
    public void setDaysOverdue(int daysOverdue) {
        this.daysOverdue = daysOverdue;
    }
}