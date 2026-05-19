package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.TaskRequestDTO;
import com.createtask.createtask.dto.response.TaskResponseDTO;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.ResourceNotFoundException;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Uses Mockito to isolate the service layer — no Spring context loaded, so tests run fast
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    // Mock all repositories so no real DB calls happen during tests
    @Mock private TaskRepository taskRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;

    // Inject the mocks into a real TaskServiceImpl instance
    @InjectMocks private TaskServiceImpl taskService;

    // Shared test fixtures — built fresh before each test
    private AppUser testUser;
    private Project testProject;
    private Task testTask;
    private TaskRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Build a sample user matching the AppUser entity field names
        testUser = new AppUser();
        testUser.setUserID(1);
        testUser.setUsername("john_doe");
        testUser.setPassword("password123");
        testUser.setEmail("john.doe@email.com");
        testUser.setFullName("John Doe");

        // Build a sample project linked to the user
        testProject = new Project();
        testProject.setProjectID(1);
        testProject.setProjectName("Project One");
        testProject.setDescription("Test project");
        testProject.setStartDate(LocalDate.of(2022, 1, 1));
        testProject.setEndDate(LocalDate.of(2022, 2, 1));
        testProject.setUser(testUser);

        // Build a sample task linked to the project and user
        testTask = new Task();
        testTask.setTaskID(1);
        testTask.setTaskName("Task One");
        testTask.setDescription("Description for Task One");
        testTask.setDueDate(LocalDate.of(2022, 1, 10));
        testTask.setPriority("High");
        testTask.setStatus("In Progress");
        testTask.setProject(testProject);
        testTask.setUser(testUser);

        // Build a matching request DTO used across multiple test cases
        requestDTO = new TaskRequestDTO();
        requestDTO.setTaskID(1);
        requestDTO.setTaskName("Task One");
        requestDTO.setDescription("Description for Task One");
        requestDTO.setDueDate(LocalDate.of(2022, 1, 10));
        requestDTO.setPriority("High");
        requestDTO.setStatus("In Progress");
        requestDTO.setProjectID(1);
        requestDTO.setUserID(1);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // createTask
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("createTask — positive: saves task and returns correct DTO")
    void createTask_success() {
        // Arrange: repositories return valid entities
        when(projectRepository.findById(1)).thenReturn(Optional.of(testProject));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // Act
        TaskResponseDTO result = taskService.createTask(requestDTO);

        // Assert: returned DTO matches the saved task
        assertThat(result.getTaskID()).isEqualTo(1);
        assertThat(result.getTaskName()).isEqualTo("Task One");
        assertThat(result.getProjectName()).isEqualTo("Project One");
        assertThat(result.getUserName()).isEqualTo("John Doe");

        // Verify save was called exactly once
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("createTask — negative: throws 404 when project ID does not exist")
    void createTask_projectNotFound() {
        // Arrange: project lookup returns empty
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        // Act + Assert: expect ResourceNotFoundException with meaningful message
        assertThatThrownBy(() -> taskService.createTask(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Project not found with ID: 1");

        // Save must never be called when a dependency is missing
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask — negative: throws 404 when user ID does not exist")
    void createTask_userNotFound() {
        // Arrange: project found but user lookup returns empty
        when(projectRepository.findById(1)).thenReturn(Optional.of(testProject));
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> taskService.createTask(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with ID: 1");

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTask — positive: creates task without project or user (both null)")
    void createTask_withoutProjectAndUser() {
        // Arrange: request has no project or user IDs
        requestDTO.setProjectID(null);
        requestDTO.setUserID(null);

        // Build a task with no project/user for the mock to return
        Task taskWithoutRefs = new Task();
        taskWithoutRefs.setTaskID(1);
        taskWithoutRefs.setTaskName("Task One");
        taskWithoutRefs.setPriority("High");
        taskWithoutRefs.setStatus("Pending");

        when(taskRepository.save(any(Task.class))).thenReturn(taskWithoutRefs);

        // Act
        TaskResponseDTO result = taskService.createTask(requestDTO);

        // Assert: DTO is returned with no project/user fields populated
        assertThat(result.getTaskID()).isEqualTo(1);
        assertThat(result.getProjectID()).isNull();
        assertThat(result.getUserID()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getTaskById
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getTaskById — positive: returns correct DTO when task exists")
    void getTaskById_success() {
        // Arrange: repository returns the test task for ID 1
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));

        // Act: fetch the task by its ID
        TaskResponseDTO result = taskService.getTaskById(1);

        // Assert: all key fields are correctly mapped from entity to DTO
        assertThat(result.getTaskID()).isEqualTo(1);
        assertThat(result.getTaskName()).isEqualTo("Task One");
        assertThat(result.getPriority()).isEqualTo("High");
        assertThat(result.getStatus()).isEqualTo("In Progress");
    }

    @Test
    @DisplayName("getTaskById — negative: throws 404 when task ID does not exist")
    void getTaskById_notFound() {
        // Arrange: no task exists with ID 99 — repository returns empty
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        // Act + Assert: service must throw ResourceNotFoundException with the correct ID
        assertThatThrownBy(() -> taskService.getTaskById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getAllTasks
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllTasks — positive: returns list of all tasks as DTOs")
    void getAllTasks_success() {
        // Arrange: one task exists in the system
        when(taskRepository.findAll()).thenReturn(List.of(testTask));

        // Act: fetch all tasks
        List<TaskResponseDTO> result = taskService.getAllTasks();

        // Assert: single task is correctly mapped and returned
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTaskName()).isEqualTo("Task One");
    }

    @Test
    @DisplayName("getAllTasks — positive: returns empty list when no tasks exist")
    void getAllTasks_empty() {
        // Arrange: repository returns nothing — no tasks seeded yet
        when(taskRepository.findAll()).thenReturn(List.of());

        // Act
        List<TaskResponseDTO> result = taskService.getAllTasks();

        // Assert: empty list returned cleanly — not treated as an error
        assertThat(result).isEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // updateTask
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateTask — positive: updates and returns the updated task DTO")
    void updateTask_success() {
        // Arrange: existing task found, project and user valid
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(projectRepository.findById(1)).thenReturn(Optional.of(testProject));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        // Change the status in the request
        requestDTO.setStatus("Completed");
        testTask.setStatus("Completed");

        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskResponseDTO result = taskService.updateTask(1, requestDTO);

        assertThat(result.getStatus()).isEqualTo("Completed");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask — negative: throws 404 when task to update does not exist")
    void updateTask_taskNotFound() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTask — negative: throws 404 when new project ID does not exist")
    void updateTask_newProjectNotFound() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        requestDTO.setProjectID(999);
        when(projectRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(1, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Project not found with ID: 999");
    }

    @Test
    @DisplayName("updateTask — negative: throws 404 when new user ID does not exist")
    void updateTask_newUserNotFound() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(projectRepository.findById(1)).thenReturn(Optional.of(testProject));
        requestDTO.setUserID(999);
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(1, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with ID: 999");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // deleteTask
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteTask — positive: deletes task and returns deleted task DTO")
    void deleteTask_success() {
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));

        TaskResponseDTO result = taskService.deleteTask(1);

        // Returned DTO should match the task that was deleted
        assertThat(result.getTaskID()).isEqualTo(1);
        assertThat(result.getTaskName()).isEqualTo("Task One");

        // Verify actual deletion was invoked
        verify(taskRepository, times(1)).delete(testTask);
    }

    @Test
    @DisplayName("deleteTask — negative: throws 404 when task to delete does not exist")
    void deleteTask_notFound() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");

        verify(taskRepository, never()).delete(any());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getTasksByProject
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getTasksByProject — positive: returns tasks under a valid project")
    void getTasksByProject_success() {
        when(projectRepository.existsById(1)).thenReturn(true);
        when(taskRepository.findByProjectID(1)).thenReturn(List.of(testTask));

        List<TaskResponseDTO> result = taskService.getTasksByProject(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProjectName()).isEqualTo("Project One");
    }

    @Test
    @DisplayName("getTasksByProject — negative: throws 404 when project does not exist")
    void getTasksByProject_projectNotFound() {
        when(projectRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> taskService.getTasksByProject(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Project not found with ID: 99");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getTasksByUser
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getTasksByUser — positive: returns tasks assigned to a valid user")
    void getTasksByUser_success() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(taskRepository.findByUserID(1)).thenReturn(List.of(testTask));

        List<TaskResponseDTO> result = taskService.getTasksByUser(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("getTasksByUser — negative: throws 404 when user does not exist")
    void getTasksByUser_userNotFound() {
        when(userRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> taskService.getTasksByUser(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with ID: 99");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getTasksByStatus
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getTasksByStatus — positive: returns tasks matching the given status")
    void getTasksByStatus_success() {
        // Arrange: one "In Progress" task exists
        when(taskRepository.findByStatus("In Progress")).thenReturn(List.of(testTask));

        // Act: filter by status
        List<TaskResponseDTO> result = taskService.getTasksByStatus("In Progress");

        // Assert: result contains the matching task with correct status
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("In Progress");
    }

    @Test
    @DisplayName("getTasksByStatus — positive: returns empty list when no tasks match status")
    void getTasksByStatus_noMatch() {
        // Arrange: no tasks have "Completed" status in the DB
        when(taskRepository.findByStatus("Completed")).thenReturn(List.of());

        // Act
        List<TaskResponseDTO> result = taskService.getTasksByStatus("Completed");

        // Assert: empty list returned cleanly
        assertThat(result).isEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getTasksByPriority
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getTasksByPriority — positive: returns tasks matching the given priority")
    void getTasksByPriority_success() {
        // Arrange: one "High" priority task exists
        when(taskRepository.findByPriority("High")).thenReturn(List.of(testTask));

        // Act: filter by priority
        List<TaskResponseDTO> result = taskService.getTasksByPriority("High");

        // Assert: result contains the matching task with correct priority
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriority()).isEqualTo("High");
    }

    @Test
    @DisplayName("getTasksByPriority — positive: returns empty list when no tasks match priority")
    void getTasksByPriority_noMatch() {
        // Arrange: no tasks have "Low" priority in the DB
        when(taskRepository.findByPriority("Low")).thenReturn(List.of());

        // Act
        List<TaskResponseDTO> result = taskService.getTasksByPriority("Low");

        // Assert: empty list returned cleanly
        assertThat(result).isEmpty();
    }
}