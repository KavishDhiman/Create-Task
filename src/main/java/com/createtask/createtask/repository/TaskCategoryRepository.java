package com.createtask.createtask.repository;

import com.createtask.createtask.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository for TaskCategory — uses composite key TaskCategoryId as the ID type
@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, TaskCategory.TaskCategoryId> {

    // Fetches all category mappings for a given task — used by GET /tasks/{id}/categories
    @Query("SELECT tc FROM TaskCategory tc WHERE tc.id.taskID = :taskID")
    List<TaskCategory> findByTaskID(@Param("taskID") int taskID);

    // Checks if a task-category mapping already exists — used to prevent duplicate assignments
    @Query("SELECT COUNT(tc) > 0 FROM TaskCategory tc WHERE tc.id.taskID = :taskID AND tc.id.categoryID = :categoryID")
    boolean existsByTaskIDAndCategoryID(@Param("taskID") int taskID, @Param("categoryID") int categoryID);
}