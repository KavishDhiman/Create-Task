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

    public TaskCategory() {
    }

    public TaskCategoryId getId() {
        return id;
    }

    public void setId(TaskCategoryId id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskCategory that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Embeddable
    public static class TaskCategoryId implements Serializable, Comparable<TaskCategoryId> {

        @Column(name = "TaskID")
        private int taskID;

        @Column(name = "CategoryID")
        private int categoryID;

        public TaskCategoryId() {
        }

        public int getTaskID() {
            return taskID;
        }

        public void setTaskID(int taskID) {
            this.taskID = taskID;
        }

        public int getCategoryID() {
            return categoryID;
        }

        public void setCategoryID(int categoryID) {
            this.categoryID = categoryID;
        }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TaskCategoryId that)) return false;
            return taskID == that.taskID && categoryID == that.categoryID;
        }

        @Override
        public int hashCode() {
            return Objects.hash(taskID, categoryID);
        }
    }
}