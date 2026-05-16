package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository for Task — standard CRUD comes from JpaRepository, custom filters are added below
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // Fetches all tasks that belong to a specific project — used by GET /projects/{id}/tasks
    @Query("SELECT t FROM Task t WHERE t.project.projectID = :projectID")
    List<Task> findByProjectID(@Param("projectID") int projectID);

    // Fetches all tasks assigned to a specific user — used by GET /users/{id}/tasks
    @Query("SELECT t FROM Task t WHERE t.user.userID = :userID")
    List<Task> findByUserID(@Param("userID") int userID);

    // Filters tasks by their status value — used by GET /tasks/status/{status}
    List<Task> findByStatus(String status);

    // Filters tasks by their priority value — used by GET /tasks/priority/{priority}
    List<Task> findByPriority(String priority);
}