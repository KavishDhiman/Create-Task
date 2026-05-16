package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.User;
import com.createtask.createtask.exception.DuplicateProjectException;
import com.createtask.createtask.exception.ProjectNotFoundException;
import com.createtask.createtask.exception.UserNotFoundException;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.ProjectService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Convert Entity -> ResponseDTO
    private ProjectResponseDTO toResponseDTO(Project project) {

        ProjectResponseDTO dto = new ProjectResponseDTO();

        dto.setProjectID(project.getProjectID());
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());

        if (project.getUser() != null) {
            dto.setUserID(project.getUser().getUserID());
            dto.setUserName(project.getUser().getUsername());
        }

        return dto;
    }

    // Convert RequestDTO -> Entity
    private Project toEntity(ProjectRequestDTO dto, User user) {

        Project project = new Project();

        project.setProjectID(dto.getProjectID());
        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setUser(user);

        return project;
    }

    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO requestDTO) {

        if (projectRepository.existsById(requestDTO.getProjectID())) {
            throw new DuplicateProjectException(
                    "Project with ID " + requestDTO.getProjectID() + " already exists."
            );
        }

        User user = userRepository.findById(requestDTO.getUserID())
                .orElseThrow(() ->
                        new UserNotFoundException(requestDTO.getUserID()));

        Project savedProject = projectRepository.save(
                toEntity(requestDTO, user)
        );

        return toResponseDTO(savedProject);
    }

    @Override
    public ProjectResponseDTO getProjectById(Integer projectID) {

        Project project = projectRepository.findById(projectID)
                .orElseThrow(() ->
                        new ProjectNotFoundException(projectID));

        return toResponseDTO(project);
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {

        List<Project> projects = projectRepository.findAll();

        List<ProjectResponseDTO> responseList = new ArrayList<>();

        for (Project project : projects) {
            responseList.add(toResponseDTO(project));
        }

        return responseList;
    }

    @Override
    public ProjectResponseDTO updateProject(Integer projectID,
                                            ProjectRequestDTO requestDTO) {

        Project existingProject = projectRepository.findById(projectID)
                .orElseThrow(() ->
                        new ProjectNotFoundException(projectID));

        User user = userRepository.findById(requestDTO.getUserID())
                .orElseThrow(() ->
                        new UserNotFoundException(requestDTO.getUserID()));

        existingProject.setProjectName(requestDTO.getProjectName());
        existingProject.setDescription(requestDTO.getDescription());
        existingProject.setStartDate(requestDTO.getStartDate());
        existingProject.setEndDate(requestDTO.getEndDate());
        existingProject.setUser(user);

        Project updatedProject = projectRepository.save(existingProject);

        return toResponseDTO(updatedProject);
    }

    @Override
    public void deleteProject(Integer projectID) {

        if (!projectRepository.existsById(projectID)) {
            throw new ProjectNotFoundException(projectID);
        }

        projectRepository.deleteById(projectID);
    }

    @Override
    public List<ProjectResponseDTO> getProjectsByUser(Integer userID) {

        if (!userRepository.existsById(userID)) {
            throw new UserNotFoundException(userID);
        }

        List<Project> projects =
                projectRepository.findByUser_UserID(userID);

        List<ProjectResponseDTO> responseList = new ArrayList<>();

        for (Project project : projects) {
            responseList.add(toResponseDTO(project));
        }

        return responseList;
    }


}