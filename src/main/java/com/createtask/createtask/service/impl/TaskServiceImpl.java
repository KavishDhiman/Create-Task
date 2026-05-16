package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.TaskRequestDTO;
import com.createtask.createtask.dto.response.TaskResponseDTO;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.User;
import com.createtask.createtask.exception.ResourceNotFoundException;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Business logic implementation for all task-related operations
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    // Handles all Task DB operations
    private final TaskRepository taskRepository;

    // Used to validate that the referenced project exists before saving a task
    private final ProjectRepository projectRepository;

    // Used to validate that the referenced user exists before saving a task
    private final UserRepository userRepository;

    // Constructor injection — preferred for testability over field injection
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Validates project and user FKs, builds the entity, persists and returns it as a DTO
    @Override
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        // Resolve project reference — throws 404 if project ID doesn't exist in DB
        Project project = null;
        if (requestDTO.getProjectID() != null) {
            project = projectRepository.findById(requestDTO.getProjectID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Project not found with ID: " + requestDTO.getProjectID()));
        }

        // Resolve user reference — throws 404 if user ID doesn't exist in DB
        User user = null;
        if (requestDTO.getUserID() != null) {
            user = userRepository.findById(requestDTO.getUserID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with ID: " + requestDTO.getUserID()));
        }

        // Map DTO fields to a new Task entity and save
        Task task = new Task();
        task.setTaskID(requestDTO.getTaskID());
        task.setTaskName(requestDTO.getTaskName());
        task.setDescription(requestDTO.getDescription());
        task.setDueDate(requestDTO.getDueDate());
        task.setPriority(requestDTO.getPriority());
        task.setStatus(requestDTO.getStatus());
        task.setProject(project);
        task.setUser(user);

        return mapToResponseDTO(taskRepository.save(task));
    }

    // Fetches the task entity and converts it — throws 404 if not found
    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(int taskID) {
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskID));
        return mapToResponseDTO(task);
    }

    // Retrieves all tasks and converts each to a response DTO
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Fetches the existing task, applies updated values from DTO, and saves back
    @Override
    public TaskResponseDTO updateTask(int taskID, TaskRequestDTO requestDTO) {
        // Ensure the task to be updated actually exists
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskID));

        // Apply scalar field updates from the incoming DTO
        task.setTaskName(requestDTO.getTaskName());
        task.setDescription(requestDTO.getDescription());
        task.setDueDate(requestDTO.getDueDate());
        task.setPriority(requestDTO.getPriority());
        task.setStatus(requestDTO.getStatus());

        // Re-validate and update project FK if a new project ID is provided
        if (requestDTO.getProjectID() != null) {
            Project project = projectRepository.findById(requestDTO.getProjectID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Project not found with ID: " + requestDTO.getProjectID()));
            task.setProject(project);
        }

        // Re-validate and update user FK if a new user ID is provided
        if (requestDTO.getUserID() != null) {
            User user = userRepository.findById(requestDTO.getUserID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with ID: " + requestDTO.getUserID()));
            task.setUser(user);
        }

        return mapToResponseDTO(taskRepository.save(task));
    }

    // Verifies the task exists before deleting — avoids silent no-ops on bad IDs
    @Override
    public void deleteTask(int taskID) {
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskID));
        taskRepository.delete(task);
    }

    // Verifies the project exists, then fetches all tasks under it
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByProject(int projectID) {
        if (!projectRepository.existsById(projectID)) {
            throw new ResourceNotFoundException("Project not found with ID: " + projectID);
        }
        return taskRepository.findByProjectID(projectID).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Verifies the user exists, then fetches all tasks assigned to them
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByUser(int userID) {
        if (!userRepository.existsById(userID)) {
            throw new ResourceNotFoundException("User not found with ID: " + userID);
        }
        return taskRepository.findByUserID(userID).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Delegates status-based filtering to the repository derived query
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Delegates priority-based filtering to the repository derived query
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByPriority(String priority) {
        return taskRepository.findByPriority(priority).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Converts a Task entity to a TaskResponseDTO — safely handles null project/user
    private TaskResponseDTO mapToResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setTaskID(task.getTaskID());
        dto.setTaskName(task.getTaskName());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());

        // Only populate project fields if the task has a project linked
        if (task.getProject() != null) {
            dto.setProjectID(task.getProject().getProjectID());
            dto.setProjectName(task.getProject().getProjectName());
        }

        // Only populate user fields if the task has a user assigned
        if (task.getUser() != null) {
            dto.setUserID(task.getUser().getUserID());
            dto.setUserName(task.getUser().getFullName());
        }

        return dto;
    }
}