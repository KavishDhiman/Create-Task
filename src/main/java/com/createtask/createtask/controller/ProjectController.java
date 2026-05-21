package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import com.createtask.createtask.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // CREATE PROJECT

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO requestDTO) {

        return ResponseEntity.ok(
                projectService.createProject(requestDTO)
        );
    }

    // GET ALL PROJECTS

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {

        return ResponseEntity.ok(
                projectService.getAllProjects()
        );
    }

    // GET PROJECT BY ID

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(
            @PathVariable Integer projectId) {

        return ResponseEntity.ok(
                projectService.getProjectById(projectId)
        );
    }

    // UPDATE PROJECT

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Integer projectId,
            @Valid @RequestBody ProjectRequestDTO requestDTO) {

        return ResponseEntity.ok(
                projectService.updateProject(projectId, requestDTO)
        );
    }

    // DELETE PROJECT

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(
            @PathVariable Integer projectId) {

        return ResponseEntity.ok(
                projectService.deleteProject(projectId)
        );
    }
    // GET PROJECTS BY USER

    @Operation(summary = "Get all projects belonging to a user")
    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByUser(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                projectService.getProjectsByUser(userId)
        );
    }

}