package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.response.OverdueTaskDTO;
import com.createtask.createtask.dto.response.ProjectSummaryDTO;
import com.createtask.createtask.dto.response.UserProductivityDTO;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Service implementation class for report-related business logic
@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository; // Repository dependency for user operations

    private final TaskRepository taskRepository; // Repository dependency for task operations

    private final ProjectRepository projectRepository; // Repository dependency for project operations

    // Constructor injection for repositories
    public ReportServiceImpl(UserRepository userRepository,
                             TaskRepository taskRepository,
                             ProjectRepository projectRepository) {

        this.userRepository = userRepository; // Assigns UserRepository object
        this.taskRepository = taskRepository; // Assigns TaskRepository object
        this.projectRepository = projectRepository; // Assigns ProjectRepository object
    }

    // Generates productivity report for all users
    @Override
    @Transactional(readOnly = true)
    public List<UserProductivityDTO> getUserProductivityReport() {

        // Retrieves all users from database
        List<AppUser> users = userRepository.findAll();

        // Converts user data into productivity report DTO list
        return users.stream()
                .sorted()
                .map(user -> {

                    // Counts total tasks assigned to user
                    long total = taskRepository.countByUserID(user.getUserID());

                    // Counts completed tasks assigned to user
                    long completed = taskRepository.countByUserIDAndStatus(user.getUserID(), "Completed");

                    // Counts pending tasks assigned to user
                    long pending = taskRepository.countByUserIDAndStatus(user.getUserID(), "Pending");

                    // Calculates completion percentage
                    double completionRate = total > 0
                            ? Math.round((completed * 100.0 / total) * 100.0) / 100.0
                            : 0.0;

                    // Creates DTO object with productivity details
                    return new UserProductivityDTO(
                            user.getUserID(),
                            user.getFullName(),
                            total,
                            completed,
                            pending,
                            completionRate
                    );
                })

                // Converts stream into list
                .collect(Collectors.toList());
    }

    // Generates summary report for all projects
    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDTO> getProjectSummaryReport() {

        // Retrieves all projects from database
        List<Project> projects = projectRepository.findAll();

        // Converts project data into summary DTO list
        return projects.stream()
                .sorted()
                .map(project -> {

                    // Stores project ID
                    int id = project.getProjectID();

                    // Counts total tasks in project
                    long total = projectRepository.countTasksByProjectID(id);

                    // Counts completed tasks in project
                    long completed = projectRepository.countTasksByProjectIDAndStatus(id, "Completed");

                    // Counts in-progress tasks in project
                    long inProgress = projectRepository.countTasksByProjectIDAndStatus(id, "In Progress");

                    // Counts pending tasks in project
                    long pending = projectRepository.countTasksByProjectIDAndStatus(id, "Pending");

                    // Calculates project completion percentage
                    double completionPercentage = total > 0
                            ? Math.round((completed * 100.0 / total) * 100.0) / 100.0
                            : 0.0;

                    // Creates DTO object with project summary details
                    return new ProjectSummaryDTO(
                            id,
                            project.getProjectName(),
                            total,
                            completed,
                            inProgress,
                            pending,
                            completionPercentage
                    );
                })

                // Converts stream into list
                .collect(Collectors.toList());
    }

    // Generates overdue task report
    @Override
    @Transactional(readOnly = true)
    public List<OverdueTaskDTO> getOverdueTasksReport(int days) {

        // Gets current date
        LocalDate today = LocalDate.now();

        // Calculates cutoff date using days input
        LocalDate cutoffDate = today.minusDays(days);

        // Retrieves overdue tasks and converts them into DTO list
        return taskRepository.findOverdueTasks(cutoffDate).stream()

                // Sorts tasks by due date
                .sorted(Comparator.comparing(Task::getDueDate))

                // Maps task entity into overdue task DTO
                .map(task -> new OverdueTaskDTO(
                        task.getTaskID(),
                        task.getTaskName(),
                        task.getDueDate(),
                        task.getUser() != null ? task.getUser().getFullName() : "",
                        task.getProject() != null ? task.getProject().getProjectName() : "",
                        (int) ChronoUnit.DAYS.between(task.getDueDate(), today)
                ))

                // Converts stream into list
                .collect(Collectors.toList());
    }
}