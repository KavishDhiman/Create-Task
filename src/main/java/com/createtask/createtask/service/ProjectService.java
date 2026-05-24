package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import java.util.List;

/*
 * Service contract for all project-related business operations.
 * Controller layer communicates only with this interface.
 */
public interface ProjectService {

    // Creates a new project using client request data.
    ProjectResponseDTO createProject(ProjectRequestDTO requestDTO);

    // Fetches project details using the project ID.
    ProjectResponseDTO getProjectById(Integer projectID);

    // Retrieves all available projects from the system.
    List<ProjectResponseDTO> getAllProjects();

    // Updates existing project details based on project ID.
    ProjectResponseDTO updateProject(Integer projectID, ProjectRequestDTO requestDTO);

    /*
     * Returns confirmation message after successful deletion.
     * Improves API response readability for the client.
     */
    String deleteProject(Integer projectID);

    // Retrieves all projects associated with a specific user.
    List<ProjectResponseDTO> getProjectsByUser(Integer userID);
}