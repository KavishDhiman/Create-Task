package com.createtask.createtask.repository;

import com.createtask.createtask.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, TaskCategory.TaskCategoryId> {
}