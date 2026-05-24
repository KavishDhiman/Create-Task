package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Handles database operations for Task entity
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // Retrieves tasks associated with a project
    @Query("SELECT t FROM Task t WHERE t.project.projectID = :projectID")
    List<Task> findByProjectID(@Param("projectID") int projectID);

    // Retrieves tasks assigned to a user
    @Query("SELECT t FROM Task t WHERE t.user.userID = :userID")
    List<Task> findByUserID(@Param("userID") int userID);

    // Retrieves tasks filtered by status
    List<Task> findByStatus(String status);

    // Retrieves tasks filtered by priority
    List<Task> findByPriority(String priority);

    // Counts total tasks assigned to a user
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.userID = :userID")
    long countByUserID(@Param("userID") int userID);

    // Counts tasks assigned to a user by status
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.userID = :userID AND t.status = :status")
    long countByUserIDAndStatus(@Param("userID") int userID, @Param("status") String status);

    // Retrieves overdue tasks before cutoff date
    @Query("SELECT t FROM Task t WHERE t.dueDate IS NOT NULL AND t.dueDate < :cutoffDate AND COALESCE(LOWER(t.status), '') <> 'completed'")
    List<Task> findOverdueTasks(@Param("cutoffDate") LocalDate cutoffDate);
}