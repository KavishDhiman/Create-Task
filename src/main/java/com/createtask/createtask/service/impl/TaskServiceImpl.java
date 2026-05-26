package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.TaskRequestDTO;
import com.createtask.createtask.dto.response.TaskResponseDTO;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.exception.ResourceNotFoundException;
import com.createtask.createtask.exception.TaskDeletionNotAllowedException;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.TaskCategoryRepository;
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

    // Used to check whether a task is still mapped to any category before delete
    private final TaskCategoryRepository taskCategoryRepository;

    // Constructor injection — preferred for testability over field injection
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository,
                           TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    // Creates a new task after validating duplicate task ID, project, user, and due date range
    @Override
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {

        if (taskRepository.existsById(requestDTO.getTaskID())) {
            throw new DuplicateResourceException(
                    "Task already exists with ID: " + requestDTO.getTaskID());
        }

        Project project = null;
        if (requestDTO.getProjectID() != null) {
            project = projectRepository.findById(requestDTO.getProjectID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Project not found with ID: " + requestDTO.getProjectID()));
        }

        AppUser user = null;
        if (requestDTO.getUserID() != null) {
            user = userRepository.findById(requestDTO.getUserID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with ID: " + requestDTO.getUserID()));
        }

        validateDueDateWithinProjectRange(requestDTO.getDueDate(), project);

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
        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskID));

        Project project = task.getProject();
        if (requestDTO.getProjectID() != null) {
            project = projectRepository.findById(requestDTO.getProjectID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Project not found with ID: " + requestDTO.getProjectID()));
            task.setProject(project);
        }

        if (requestDTO.getUserID() != null) {
            AppUser user = userRepository.findById(requestDTO.getUserID())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with ID: " + requestDTO.getUserID()));
            task.setUser(user);
        }

        validateDueDateWithinProjectRange(requestDTO.getDueDate(), project);

        task.setTaskName(requestDTO.getTaskName());
        task.setDescription(requestDTO.getDescription());
        task.setDueDate(requestDTO.getDueDate());
        task.setPriority(requestDTO.getPriority());
        task.setStatus(requestDTO.getStatus());

        return mapToResponseDTO(taskRepository.save(task));
    }

    // Captures the task details first, deletes it, then returns the deleted task as DTO
    @Override
    public TaskResponseDTO deleteTask(int taskID) {

        Task task = taskRepository.findById(taskID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with ID: " + taskID));

        if (!taskCategoryRepository.findByTaskID(taskID).isEmpty()) {
            throw new TaskDeletionNotAllowedException(
                    "Task cannot be deleted because it is mapped to one or more categories. " +
                            "Please remove all category mappings before deleting this task."
            );
        }

        taskRepository.delete(task);

        TaskResponseDTO response = new TaskResponseDTO();
        response.setMessage("Task with ID " + taskID + " deleted successfully");
        return response;
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

    // Validates that due date lies within the selected project's start and end dates
    private void validateDueDateWithinProjectRange(java.time.LocalDate dueDate, Project project) {
        if (dueDate == null || project == null) {
            return;
        }

        if (project.getStartDate() == null || project.getEndDate() == null) {
            return;
        }

        if (dueDate.isBefore(project.getStartDate()) || dueDate.isAfter(project.getEndDate())) {
            throw new IllegalArgumentException(
                    "Task due date must be between " +
                            project.getStartDate() +
                            " and " +
                            project.getEndDate()
            );
        }
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

        if (task.getProject() != null) {
            dto.setProjectID(task.getProject().getProjectID());
            dto.setProjectName(task.getProject().getProjectName());
        }

        if (task.getUser() != null) {
            dto.setUserID(task.getUser().getUserID());
            dto.setUserName(task.getUser().getFullName());
        }

        return dto;
    }
}