package com.createtask.createtask.dto;

import jakarta.validation.constraints.NotNull;

public class TaskCategoryDTO {

    @NotNull(message = "Task ID is required")
    private Integer taskID;

    @NotNull(message = "Category ID is required")
    private Integer categoryID;

    public TaskCategoryDTO() {
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }
}