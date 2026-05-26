package com.createtask.createtask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents task details in the system.
 */
@Entity
@Table(name = "Task")
public class Task implements Comparable<Task> {

    // Unique identifier for the task
    @Id
    @Column(name = "TaskID")
    private int taskID;

    // Name/title of the task
    @Column(name = "TaskName", nullable = false, length = 255)
    private String taskName;

    // Detailed task description
    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    // Due date assigned for the task
    @Column(name = "DueDate")
    private LocalDate dueDate;

    // Priority level: High / Medium / Low
    @Column(name = "Priority", length = 20)
    private String priority;

    // Current task status
    @Column(name = "Status", length = 20)
    private String status;

    // Associated project reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectID")
    private Project project;

    // Assigned user reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private AppUser user;

    // Default constructor required by JPA
    public Task() {
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    // Compares tasks using task ID
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