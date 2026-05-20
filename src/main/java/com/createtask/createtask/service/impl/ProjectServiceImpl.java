package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.DuplicateProjectException;
import com.createtask.createtask.exception.ProjectNotFoundException;
import com.createtask.createtask.exception.UserNotFoundException;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Holds all the actual business logic for project operations.
// @Service marks it as a Spring-managed bean so it can be injected anywhere.
@Service
public class ProjectServiceImpl implements ProjectService {

    // We inject both repos because a project is always linked to a user.
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository    = userRepository;
    }

    // Converts a Project entity into a response DTO — keeps entity internals away from the client.
    private ProjectResponseDTO toResponseDTO(Project project) {

        Integer userId = null;
        String username = null;

        if (project.getUser() != null) {

            userId = project.getUser().getUserID();
            username = project.getUser().getUsername();
        }

        return new ProjectResponseDTO(
                project.getProjectID(),
                project.getProjectName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                userId,
                username
        );
    }

    // Converts incoming request DTO into a Project entity ready to be saved.
    private Project toEntity(ProjectRequestDTO dto, AppUser user) {
        Project project = new Project();
        project.setProjectID(dto.getProjectID());
        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setUser(user);
        return project;
    }

    // Creates a new project after verifying the user exists and the project ID isn't already taken.
    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO requestDTO) {

        if (projectRepository.existsById(requestDTO.getProjectID())) {
            throw new DuplicateProjectException(
                    "A project with ID " + requestDTO.getProjectID() +
                            " already exists. Please use a different project ID."
            );
        }

        AppUser user = userRepository.findById(requestDTO.getUserID())
                .orElseThrow(() -> new UserNotFoundException(requestDTO.getUserID()));

        Project saved = projectRepository.save(toEntity(requestDTO, user));
        return toResponseDTO(saved);
    }

    // Fetches a single project by ID — throws a descriptive error if it doesn't exist.
    @Override
    public ProjectResponseDTO getProjectById(Integer projectID) {
        Project project = projectRepository.findById(projectID)
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project with ID " + projectID +
                                " was not found. Please check the project ID and try again."
                ));
        return toResponseDTO(project);
    }

    // Returns every project in the system — loops through and maps each to a response DTO.
    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponseDTO> response = new ArrayList<>();
        for (Project p : projects) {
            response.add(toResponseDTO(p));
        }
        return response;
    }

    // Updates an existing project — verifies both project and new owner user exist before saving.
    @Override
    public ProjectResponseDTO updateProject(Integer projectID, ProjectRequestDTO requestDTO) {

        Project existing = projectRepository.findById(projectID)
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project with ID " + projectID +
                                " was not found. Cannot update a project that does not exist."
                ));

        AppUser user = userRepository.findById(requestDTO.getUserID())
                .orElseThrow(() -> new UserNotFoundException(requestDTO.getUserID()));

        // Updating fields on the existing entity preserves the same DB row — no duplicate created.
        existing.setProjectName(requestDTO.getProjectName());
        existing.setDescription(requestDTO.getDescription());
        existing.setStartDate(requestDTO.getStartDate());
        existing.setEndDate(requestDTO.getEndDate());
        existing.setUser(user);

        Project updated = projectRepository.save(existing);
        return toResponseDTO(updated);
    }

    // Deletes the project and returns a confirmation string instead of void.
    @Override
    public String deleteProject(Integer projectID) {
        if (!projectRepository.existsById(projectID)) {
            throw new ProjectNotFoundException(
                    "Project with ID " + projectID +
                            " was not found. Cannot delete a project that does not exist."
            );
        }
        projectRepository.deleteById(projectID);
        return "Project with ID " + projectID + " has been deleted successfully.";
    }

    // Returns all projects belonging to a specific user — checks user exists first.
    @Override
    public List<ProjectResponseDTO> getProjectsByUser(Integer userID) {

        if (!userRepository.existsById(userID)) {
            throw new UserNotFoundException(userID);
        }

        List<Project> projects = projectRepository.findByUser_UserID(userID);
        List<ProjectResponseDTO> response = new ArrayList<>();
        for (Project p : projects) {
            response.add(toResponseDTO(p));
        }
        return response;
    }
}