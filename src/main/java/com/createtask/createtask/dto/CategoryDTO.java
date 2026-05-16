package com.createtask.createtask.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    private int categoryID;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    public CategoryDTO() {
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}