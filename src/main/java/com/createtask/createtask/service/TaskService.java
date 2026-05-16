package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.TaskRequestDTO;
import com.createtask.createtask.dto.response.TaskResponseDTO;

import java.util.List;

// Contract for all task business operations — implemented by TaskServiceImpl
public interface TaskService {

    // Creates and saves a new task after validating referenced project and user
    TaskResponseDTO createTask(TaskRequestDTO requestDTO);

    // Retrieves a single task by ID — throws 404 if not found
    TaskResponseDTO getTaskById(int taskID);

    // Retrieves all tasks in the system
    List<TaskResponseDTO> getAllTasks();

    // Updates an existing task's details — throws 404 if task does not exist
    TaskResponseDTO updateTask(int taskID, TaskRequestDTO requestDTO);

    // Deletes a task by ID — throws 404 if task does not exist
    void deleteTask(int taskID);

    // Retrieves all tasks under a specific project — used by GET /projects/{id}/tasks
    List<TaskResponseDTO> getTasksByProject(int projectID);

    // Retrieves all tasks assigned to a specific user — used by GET /users/{id}/tasks
    List<TaskResponseDTO> getTasksByUser(int userID);

    // Filters tasks by their status — used by GET /tasks/status/{status}
    List<TaskResponseDTO> getTasksByStatus(String status);

    // Filters tasks by their priority — used by GET /tasks/priority/{priority}
    List<TaskResponseDTO> getTasksByPriority(String priority);
}