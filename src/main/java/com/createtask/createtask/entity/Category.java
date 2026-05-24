package com.createtask.createtask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

// Represents category details in the system
@Entity
@Table(name = "Category")
public class Category implements Comparable<Category> {

    @Id
    @Column(name = "CategoryID")
    private int categoryID;

    @NotBlank(message = "Category name is required")
    @Column(name = "CategoryName", nullable = false, length = 255)
    private String categoryName;

    // Default constructor for Category entity
    public Category() {
    }

    // Retrieves category ID
    public int getCategoryID() {
        return categoryID;
    }

    // Sets category ID
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    // Retrieves category name
    public String getCategoryName() {
        return categoryName;
    }

    // Sets category name
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // Compares categories based on category ID
    @Override
    public int compareTo(Category other) {
        if (other == null) {
            return 1;
        }
        return Integer.compare(this.categoryID, other.categoryID);
    }

    // Checks equality using category ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return categoryID == category.categoryID;
    }

    // Generates hash code using category ID
    @Override
    public int hashCode() {
        return Objects.hash(categoryID);
    }
}