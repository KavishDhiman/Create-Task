package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import java.util.List;

// Contract that defines what operations the project service must support.
// Controller only talks to this interface — never directly to the implementation.
public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO requestDTO);

    ProjectResponseDTO getProjectById(Integer projectID);

    List<ProjectResponseDTO> getAllProjects();

    ProjectResponseDTO updateProject(Integer projectID, ProjectRequestDTO requestDTO);

    // Changed from void to String so the caller gets a clear confirmation message back.
    String deleteProject(Integer projectID);

    List<ProjectResponseDTO> getProjectsByUser(Integer userID);
}