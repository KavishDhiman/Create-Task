package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for creating a Category.
 */
public class CategoryRequestDTO {

    /*
     * @Positive rejects 0 and negative values.
     */
    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be a positive number")
    private Integer categoryID;

    /**
     * @NotBlank covers null, empty string, and whitespace-only strings.
     */
    @NotBlank(message = "Category name is required")
    private String categoryName;

    // ── Constructors ──────────────────────────────────────────────────────────

    public CategoryRequestDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Integer getCategoryID() { return categoryID; }
    public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}