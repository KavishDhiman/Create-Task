package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO to receive category creation data from the client
public class CategoryRequestDTO {

    // Client must supply the ID manually (matches DB script — no auto-increment)
    @NotNull(message = "Category ID is required")
    private Integer categoryID;

    // Category must have a name — blank strings are rejected
    @NotBlank(message = "Category name is required")
    private String categoryName;

    public CategoryRequestDTO() {}

    public Integer getCategoryID() { return categoryID; }
    public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}