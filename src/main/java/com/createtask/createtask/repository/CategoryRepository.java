package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Handles database operations for Category entity
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}