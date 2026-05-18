package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.ProjectRequestDTO;
import com.createtask.createtask.dto.response.ProjectResponseDTO;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.User;
import com.createtask.createtask.exception.DuplicateProjectException;
import com.createtask.createtask.exception.ProjectNotFoundException;
import com.createtask.createtask.exception.UserNotFoundException;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Mockito handles all DB interactions with fake responses — no real DB calls happen here.
// @ExtendWith wires Mockito into JUnit 5 without needing the full Spring context.
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    // @InjectMocks creates the real ProjectServiceImpl and injects the mocks above into it.
    @InjectMocks
    private ProjectServiceImpl projectService;

    private User testUser;
    private Project testProject;
    private ProjectRequestDTO requestDTO;

    // Sets up reusable test data before every single test method runs.
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserID(1);
        testUser.setUsername("john_doe");
        testUser.setEmail("john.doe@email.com");
        testUser.setFullName("John Doe");
        testUser.setPassword("password123");

        testProject = new Project();
        testProject.setProjectID(1);
        testProject.setProjectName("Project One");
        testProject.setDescription("Description for Project One");
        testProject.setStartDate(LocalDate.of(2022, 1, 1));
        testProject.setEndDate(LocalDate.of(2022, 2, 1));
        testProject.setUser(testUser);

        requestDTO = new ProjectRequestDTO();
        requestDTO.setProjectID(1);
        requestDTO.setProjectName("Project One");
        requestDTO.setDescription("Description for Project One");
        requestDTO.setStartDate(LocalDate.of(2022, 1, 1));
        requestDTO.setEndDate(LocalDate.of(2022, 2, 1));
        requestDTO.setUserID(1);
    }

    // ===========================================================
    // CREATE PROJECT
    // ===========================================================

    @Test
    void testCreateProject_Success() {
        // Mock: project ID is free, user exists, save returns the project.
        when(projectRepository.existsById(1)).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        ProjectResponseDTO response = projectService.createProject(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getProjectID()).isEqualTo(1);
        assertThat(response.getProjectName()).isEqualTo("Project One");
        assertThat(response.getUserID()).isEqualTo(1);
        assertThat(response.getUserName()).isEqualTo("john_doe");
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testCreateProject_DuplicateID_ThrowsDuplicateProjectException() {
        // Mock: project ID already taken — exception fires before save is ever called.
        when(projectRepository.existsById(1)).thenReturn(true);

        assertThatThrownBy(() -> projectService.createProject(requestDTO))
                .isInstanceOf(DuplicateProjectException.class)
                .hasMessageContaining("already exists");

        verify(projectRepository, never()).save(any());
    }

    @Test
    void testCreateProject_InvalidUserID_ThrowsUserNotFoundException() {
        // Mock: project ID is free but the user doesn't exist — save should never be reached.
        when(projectRepository.existsById(1)).thenReturn(false);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.createProject(requestDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID");

        verify(projectRepository, never()).save(any());
    }

    // ===========================================================
    // GET PROJECT BY ID
    // ===========================================================

    @Test
    void testGetProjectById_Success() {
        // Mock: project exists — should map cleanly to a populated response DTO.
        when(projectRepository.findById(1)).thenReturn(Optional.of(testProject));

        ProjectResponseDTO response = projectService.getProjectById(1);

        assertThat(response).isNotNull();
        assertThat(response.getProjectID()).isEqualTo(1);
        assertThat(response.getProjectName()).isEqualTo("Project One");
    }

    @Test
    void testGetProjectById_NotFound_ThrowsProjectNotFoundException() {
        // Mock: no project for this ID — exception should carry the ID in the message.
        when(projectRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProjectById(99))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessageContaining("was not found");
    }

    // ===========================================================
    // GET ALL PROJECTS
    // ===========================================================

    @Test
    void testGetAllProjects_Success() {
        // Mock: two projects exist — list size and names should match exactly.
        Project secondProject = new Project();
        secondProject.setProjectID(2);
        secondProject.setProjectName("Project Two");
        secondProject.setStartDate(LocalDate.of(2022, 2, 1));
        secondProject.setUser(testUser);

        when(projectRepository.findAll()).thenReturn(Arrays.asList(testProject, secondProject));

        List<ProjectResponseDTO> response = projectService.getAllProjects();

        assertThat(response).hasSize(2);
        assertThat(response.get(0).getProjectName()).isEqualTo("Project One");
        assertThat(response.get(1).getProjectName()).isEqualTo("Project Two");
    }

    @Test
    void testGetAllProjects_EmptyList() {
        // Mock: DB has no projects — should return empty list without throwing.
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProjectResponseDTO> response = projectService.getAllProjects();

        assertThat(response).isEmpty();
    }

    // ===========================================================
    // UPDATE PROJECT
    // ===========================================================

    @Test
    void testUpdateProject_Success() {
        // Mock: project and user both exist — updated values should reflect in the response.
        ProjectRequestDTO updateDTO = new ProjectRequestDTO();
        updateDTO.setProjectName("Updated Project One");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStartDate(LocalDate.of(2022, 3, 1));
        updateDTO.setEndDate(LocalDate.of(2022, 5, 1));
        updateDTO.setUserID(1);

        Project updatedProject = new Project();
        updatedProject.setProjectID(1);
        updatedProject.setProjectName("Updated Project One");
        updatedProject.setDescription("Updated Description");
        updatedProject.setStartDate(LocalDate.of(2022, 3, 1));
        updatedProject.setEndDate(LocalDate.of(2022, 5, 1));
        updatedProject.setUser(testUser);

        when(projectRepository.findById(1)).thenReturn(Optional.of(testProject));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        ProjectResponseDTO response = projectService.updateProject(1, updateDTO);

        assertThat(response.getProjectName()).isEqualTo("Updated Project One");
        assertThat(response.getDescription()).isEqualTo("Updated Description");
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testUpdateProject_ProjectNotFound_ThrowsProjectNotFoundException() {
        // Mock: project doesn't exist — update should throw before touching the user repo.
        when(projectRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.updateProject(99, requestDTO))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(projectRepository, never()).save(any());
    }

    @Test
    void testUpdateProject_UserNotFound_ThrowsUserNotFoundException() {
        // Mock: project exists but the new owner user ID is invalid.
        when(projectRepository.findById(1)).thenReturn(Optional.of(testProject));
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.updateProject(1, requestDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID");

        verify(projectRepository, never()).save(any());
    }

    // ===========================================================
    // DELETE PROJECT
    // ===========================================================

    @Test
    void testDeleteProject_Success() {
        // Mock: project exists — delete runs and returns a confirmation string.
        when(projectRepository.existsById(1)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1);

        String result = projectService.deleteProject(1);

        assertThat(result).contains("deleted successfully");
        assertThat(result).contains("1");
        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteProject_NotFound_ThrowsProjectNotFoundException() {
        // Mock: project doesn't exist — deleteById should never be called.
        when(projectRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> projectService.deleteProject(99))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessageContaining("was not found");

        verify(projectRepository, never()).deleteById(any());
    }

    // ===========================================================
    // GET PROJECTS BY USER
    // ===========================================================

    @Test
    void testGetProjectsByUser_Success() {
        // Mock: user exists and has one project — response list should have exactly one entry.
        when(userRepository.existsById(1)).thenReturn(true);
        when(projectRepository.findByUser_UserID(1))
                .thenReturn(Collections.singletonList(testProject));

        List<ProjectResponseDTO> response = projectService.getProjectsByUser(1);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getProjectID()).isEqualTo(1);
        assertThat(response.get(0).getUserID()).isEqualTo(1);
    }

    @Test
    void testGetProjectsByUser_UserNotFound_ThrowsUserNotFoundException() {
        // Mock: user doesn't exist — should throw before even querying the project repo.
        when(userRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> projectService.getProjectsByUser(99))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID");

        verify(projectRepository, never()).findByUser_UserID(any());
    }

    @Test
    void testGetProjectsByUser_NoProjects_ReturnsEmptyList() {
        // Mock: user exists but owns no projects — empty list is valid, not an error.
        when(userRepository.existsById(1)).thenReturn(true);
        when(projectRepository.findByUser_UserID(1))
                .thenReturn(Collections.emptyList());

        List<ProjectResponseDTO> response = projectService.getProjectsByUser(1);

        assertThat(response).isEmpty();
    }
}