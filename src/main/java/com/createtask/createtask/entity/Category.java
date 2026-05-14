package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Category")
public class Category {

    @Id
    @Column(name = "CategoryID")
    private int categoryID;

    @Column(name = "CategoryName", nullable = false, length = 255)
    private String categoryName;
}

