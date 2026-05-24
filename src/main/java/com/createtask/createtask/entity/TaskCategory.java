package com.createtask.createtask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

// Represents mapping between tasks and categories
@Entity
@Table(name = "TaskCategory")
public class TaskCategory implements Comparable<TaskCategory> {

    @EmbeddedId
    private TaskCategoryId id;

    @NotNull(message = "Task is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskID")
    @JoinColumn(name = "TaskID")
    private Task task;

    @NotNull(message = "Category is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryID")
    @JoinColumn(name = "CategoryID")
    private Category category;

    // Default constructor for TaskCategory entity
    public TaskCategory() {
    }

    // Retrieves composite mapping ID
    public TaskCategoryId getId() {
        return id;
    }

    // Sets composite mapping ID
    public void setId(TaskCategoryId id) {
        this.id = id;
    }

    // Retrieves associated task details
    public Task getTask() {
        return task;
    }

    // Sets associated task details
    public void setTask(Task task) {
        this.task = task;
    }

    // Retrieves associated category details
    public Category getCategory() {
        return category;
    }

    // Sets associated category details
    public void setCategory(Category category) {
        this.category = category;
    }

    // Compares task-category mappings using composite ID
    @Override
    public int compareTo(TaskCategory other) {
        if (other == null) {
            return 1;
        }
        if (this.id == null && other.id == null) return 0;
        if (this.id == null) return -1;
        if (other.id == null) return 1;
        return this.id.compareTo(other.id);
    }

    // Checks equality using composite ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskCategory that)) return false;
        return Objects.equals(id, that.id);
    }

    // Generates hash code using composite ID
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Represents composite key for task-category mapping
    @Embeddable
    public static class TaskCategoryId implements Serializable, Comparable<TaskCategoryId> {

        @Column(name = "TaskID")
        private int taskID;

        @Column(name = "CategoryID")
        private int categoryID;

        // Default constructor for composite key
        public TaskCategoryId() {
        }

        // Retrieves task ID from composite key
        public int getTaskID() {
            return taskID;
        }

        // Sets task ID in composite key
        public void setTaskID(int taskID) {
            this.taskID = taskID;
        }

        // Retrieves category ID from composite key
        public int getCategoryID() {
            return categoryID;
        }

        // Sets category ID in composite key
        public void setCategoryID(int categoryID) {
            this.categoryID = categoryID;
        }

        // Compares composite keys using task and category IDs
        @Override
        public int compareTo(TaskCategoryId other) {
            if (other == null) {
                return 1;
            }
            int first = Integer.compare(this.taskID, other.taskID);
            if (first != 0) {
                return first;
            }
            return Integer.compare(this.categoryID, other.categoryID);
        }

        // Checks equality using task and category IDs
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TaskCategoryId that)) return false;
            return taskID == that.taskID && categoryID == that.categoryID;
        }

        // Generates hash code using task and category IDs
        @Override
        public int hashCode() {
            return Objects.hash(taskID, categoryID);
        }
    }
}