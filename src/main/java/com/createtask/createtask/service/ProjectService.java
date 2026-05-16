package com.createtask.createtask.service;

import com.createtask.createtask.dto.response.ProjectResponseDTO;
import com.createtask.createtask.dto.request.ProjectRequestDTO;
import java.util.List;

// Contract that defines what operations the project service must support.
// The controller only talks to this interface — never directly to the implementation.
public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO requestDTO);

    ProjectResponseDTO getProjectById(Integer projectID);

    List<ProjectResponseDTO> getAllProjects();

    ProjectResponseDTO updateProject(Integer projectID, ProjectRequestDTO requestDTO);

    void deleteProject(Integer projectID);

    List<ProjectResponseDTO> getProjectsByUser(Integer userID);

}