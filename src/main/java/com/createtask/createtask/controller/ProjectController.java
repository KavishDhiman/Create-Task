package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import com.createtask.createtask.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * REST Controller handling all project-related API requests.
 * Delegates business operations to the service layer.
 */
@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    /*
     * Dependency Injection keeps the controller
     * loosely coupled with the service layer.
     */
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // ==================== CREATE PROJECT ====================

    /*
     * Creates a new project using validated request data.
     * Returns the created project details to the client.
     */
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO requestDTO) {

        return ResponseEntity.ok(
                projectService.createProject(requestDTO)
        );
    }

    // ==================== GET ALL PROJECTS ====================

    // Retrieves all available projects from the system.
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {

        return ResponseEntity.ok(
                projectService.getAllProjects()
        );
    }

    // ==================== GET PROJECT BY ID ====================

    // Retrieves project details using the provided project ID.
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(
            @PathVariable Integer projectId) {

        return ResponseEntity.ok(
                projectService.getProjectById(projectId)
        );
    }

    // ==================== UPDATE PROJECT ====================

    /*
     * Updates an existing project using validated request data.
     * Existing project details are replaced with updated values.
     */
    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Integer projectId,
            @Valid @RequestBody ProjectRequestDTO requestDTO) {

        return ResponseEntity.ok(
                projectService.updateProject(projectId, requestDTO)
        );
    }

    // ==================== DELETE PROJECT ====================

    // Deletes the specified project and returns confirmation message.
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(
            @PathVariable Integer projectId) {

        return ResponseEntity.ok(
                projectService.deleteProject(projectId)
        );
    }

    // ==================== GET PROJECTS BY USER ====================

    @Operation(summary = "Get all projects belonging to a user")

    /*
     * Fetches all projects associated with a specific user.
     * Supports user-based project filtering functionality.
     */
    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByUser(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                projectService.getProjectsByUser(userId)
        );
    }
}