package com.createtask.createtask.repository;

import com.createtask.createtask.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Handles database operations for TaskCategory entity
@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, TaskCategory.TaskCategoryId> {

    // Retrieves all category mappings for a specific task
    @Query("SELECT tc FROM TaskCategory tc WHERE tc.id.taskID = :taskID")
    List<TaskCategory> findByTaskID(@Param("taskID") int taskID);

    // Checks whether task-category mapping already exists
    @Query("SELECT COUNT(tc) > 0 FROM TaskCategory tc WHERE tc.id.taskID = :taskID AND tc.id.categoryID = :categoryID")
    boolean existsByTaskIDAndCategoryID(@Param("taskID") int taskID, @Param("categoryID") int categoryID);
}