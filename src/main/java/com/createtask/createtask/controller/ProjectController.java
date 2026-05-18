package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import com.createtask.createtask.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Exposes all project HTTP endpoints — every method delegates straight to the service layer.
// @Tag groups these endpoints together neatly inside Swagger UI.
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Project Management", description = "APIs for creating, updating, fetching and deleting projects")
public class ProjectController {

    // Controller only depends on the interface — makes it easy to swap implementations later.
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // POST /api/v1/projects — validates the body first, then hands off to service.
    @Operation(summary = "Create a new project",
            description = "Creates a new project and links it to the specified user.")
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO requestDTO) {
        ProjectResponseDTO response = projectService.createProject(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/v1/projects/{projectId} — returns one project's full details.
    @Operation(summary = "Get project by ID",
            description = "Returns the details of a single project using its unique ID.")
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(
            @PathVariable Integer projectId) {
        ProjectResponseDTO response = projectService.getProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/projects — returns the full list of all projects in the system.
    @Operation(summary = "Get all projects",
            description = "Returns a list of all projects currently stored in the system.")
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> response = projectService.getAllProjects();
        return ResponseEntity.ok(response);
    }

    // PUT /api/v1/projects/{projectId} — replaces project details with new values from request.
    @Operation(summary = "Update a project",
            description = "Updates the name, description, dates, or owner of an existing project.")
    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Integer projectId,
            @Valid @RequestBody ProjectRequestDTO requestDTO) {
        ProjectResponseDTO response = projectService.updateProject(projectId, requestDTO);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/v1/projects/{projectId} — now returns the confirmation string from service.
    @Operation(summary = "Delete a project",
            description = "Permanently deletes the project with the given ID from the system.")
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer projectId) {
        String message = projectService.deleteProject(projectId);
        return ResponseEntity.ok(message);
    }

    // GET /api/v1/users/{userId}/projects — lists all projects owned by a particular user.
    @Operation(summary = "Get projects by user",
            description = "Returns all projects that are owned by the specified user.")
    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByUser(
            @PathVariable Integer userId) {
        List<ProjectResponseDTO> response = projectService.getProjectsByUser(userId);
        return ResponseEntity.ok(response);
    }
}