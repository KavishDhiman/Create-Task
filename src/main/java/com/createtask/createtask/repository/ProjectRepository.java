package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    // Used in GET /api/v1/users/{userId}/projects
    List<Project> findByUser_UserID(Integer userID);

    // Counts total tasks under a project — used by project summary report
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectID = :projectID")
    long countTasksByProjectID(@Param("projectID") int projectID);

    // Counts tasks under a project filtered by status — used by project summary report
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectID = :projectID AND t.status = :status")
    long countTasksByProjectIDAndStatus(@Param("projectID") int projectID,
                                        @Param("status") String status);
}