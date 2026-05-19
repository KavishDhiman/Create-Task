package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Task entity.
 * Standard CRUD comes from JpaRepository.
 * Custom filters and aggregations added below for task management and reporting.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    /** Fetches all tasks that belong to a specific project — GET /projects/{id}/tasks */
    @Query("SELECT t FROM Task t WHERE t.project.projectID = :projectID")
    List<Task> findByProjectID(@Param("projectID") int projectID);

    /** Fetches all tasks assigned to a specific user — GET /users/{id}/tasks */
    @Query("SELECT t FROM Task t WHERE t.user.userID = :userID")
    List<Task> findByUserID(@Param("userID") int userID);

    /** Filters tasks by status value — GET /tasks/status/{status} */
    List<Task> findByStatus(String status);

    /** Filters tasks by priority value — GET /tasks/priority/{priority} */
    List<Task> findByPriority(String priority);

    /** Counts total tasks assigned to a user — used by productivity report */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.userID = :userID")
    long countByUserID(@Param("userID") int userID);

    /** Counts tasks for a user filtered by a specific status — used by productivity report */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.userID = :userID AND t.status = :status")
    long countByUserIDAndStatus(@Param("userID") int userID, @Param("status") String status);

    /** Fetches overdue tasks before a cutoff date — used by overdue task report */
    @Query("SELECT t FROM Task t WHERE t.dueDate IS NOT NULL AND t.dueDate < :cutoffDate AND COALESCE(LOWER(t.status), '') <> 'completed'")
    List<Task> findOverdueTasks(@Param("cutoffDate") LocalDate cutoffDate);
}