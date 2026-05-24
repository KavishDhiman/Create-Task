package com.createtask.createtask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Objects;

// Represents task details in the system
@Entity
@Table(name = "Task")
public class Task implements Comparable<Task> {

    @Id
    @Column(name = "TaskID")
    private int taskID;

    @NotBlank(message = "Task name is required")
    @Column(name = "TaskName", nullable = false, length = 255)
    private String taskName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "DueDate")
    private LocalDate dueDate;

    @Column(name = "Priority", length = 20)
    private String priority;

    @Column(name = "Status", length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private AppUser user;

    // Default constructor for Task entity
    public Task() {
    }

    // Retrieves task ID
    public int getTaskID() {
        return taskID;
    }

    // Sets task ID
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    // Retrieves task name
    public String getTaskName() {
        return taskName;
    }

    // Sets task name
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Retrieves task description
    public String getDescription() {
        return description;
    }

    // Sets task description
    public void setDescription(String description) {
        this.description = description;
    }

    // Retrieves task due date
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Sets task due date
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Retrieves task priority
    public String getPriority() {
        return priority;
    }

    // Sets task priority
    public void setPriority(String priority) {
        this.priority = priority;
    }

    // Retrieves task status
    public String getStatus() {
        return status;
    }

    // Sets task status
    public void setStatus(String status) {
        this.status = status;
    }

    // Retrieves associated project details
    public Project getProject() {
        return project;
    }

    // Sets associated project details
    public void setProject(Project project) {
        this.project = project;
    }

    // Retrieves assigned user details
    public AppUser getUser() {
        return user;
    }

    // Sets assigned user details
    public void setUser(AppUser user) {
        this.user = user;
    }

    // Compares tasks based on task ID
    @Override
    public int compareTo(Task other) {
        if (other == null) {
            return 1;
        }
        return Integer.compare(this.taskID, other.taskID);
    }

    // Checks equality using task ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return taskID == task.taskID;
    }

    // Generates hash code using task ID
    @Override
    public int hashCode() {
        return Objects.hash(taskID);
    }
}