package com.createtask.createtask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "Category")
public class Category implements Comparable<Category> {

    @Id
    @Column(name = "CategoryID")
    private int categoryID;

    @NotBlank(message = "Category name is required")
    @Column(name = "CategoryName", nullable = false, length = 255)
    private String categoryName;

    public Category() {
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

    @Override
    public int compareTo(Category other) {
        if (other == null) {
            return 1;
        }
        return Integer.compare(this.categoryID, other.categoryID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return categoryID == category.categoryID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryID);
    }
}