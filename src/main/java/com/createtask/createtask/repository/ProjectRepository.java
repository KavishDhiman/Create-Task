package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Repository layer responsible for abstracting all Project-related
 * database operations from the business and controller layers.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /*
     * Retrieves all projects associated with a specific user.
     * Supports user-specific project segregation in the application.
     */
    List<Project> findByUser_UserID(Integer userID);

    /*
     * Aggregates total task count for a given project.
     * Used in dashboard analytics and project summary generation.
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectID = :projectID")
    long countTasksByProjectID(@Param("projectID") int projectID);

    /*
     * Retrieves task count based on project and task status.
     * Enables status-wise reporting for business monitoring.
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectID = :projectID AND t.status = :status")
    long countTasksByProjectIDAndStatus(@Param("projectID") int projectID,
                                        @Param("status") String status);
}