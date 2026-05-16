package com.createtask.createtask.dto.response;

// DTO to send category details back to the client
public class CategoryResponseDTO {

    // Unique identifier of the category
    private int categoryID;

    // Display name of the category
    private String categoryName;

    public CategoryResponseDTO() {}

    public int getCategoryID() { return categoryID; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}