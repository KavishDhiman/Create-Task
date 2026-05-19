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

/**
 * Implements all reporting logic by aggregating existing data — no schema changes needed.
 * Both reports reuse repositories already present in the project.
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    /** Added ProjectRepository to support the project summary report. */
    private final ProjectRepository projectRepository;

    // Creates the reporting service with all required repositories.
    public ReportServiceImpl(UserRepository userRepository,
                             TaskRepository taskRepository,
                             ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    /** Fetches all users, aggregates their task counts, and returns productivity metrics. */
    @Override
    @Transactional(readOnly = true)
    public List<UserProductivityDTO> getUserProductivityReport() {
        List<AppUser> users = userRepository.findAll();

        return users.stream()
                .sorted()
                .map(user -> {
                    long total = taskRepository.countByUserID(user.getUserID());
                    long completed = taskRepository.countByUserIDAndStatus(user.getUserID(), "Completed");
                    long pending = taskRepository.countByUserIDAndStatus(user.getUserID(), "Pending");

                    double completionRate = total > 0
                            ? Math.round((completed * 100.0 / total) * 100.0) / 100.0
                            : 0.0;

                    return new UserProductivityDTO(
                            user.getUserID(),
                            user.getFullName(),
                            total,
                            completed,
                            pending,
                            completionRate
                    );
                })
                .collect(Collectors.toList());
    }

    /** Fetches all projects, then counts tasks by status for each project. */
    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDTO> getProjectSummaryReport() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .sorted()
                .map(project -> {
                    int id = project.getProjectID();

                    long total = projectRepository.countTasksByProjectID(id);
                    long completed = projectRepository.countTasksByProjectIDAndStatus(id, "Completed");
                    long inProgress = projectRepository.countTasksByProjectIDAndStatus(id, "In Progress");
                    long pending = projectRepository.countTasksByProjectIDAndStatus(id, "Pending");

                    double completionPercentage = total > 0
                            ? Math.round((completed * 100.0 / total) * 100.0) / 100.0
                            : 0.0;

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
                .collect(Collectors.toList());
    }

    /** Fetches tasks overdue beyond the requested number of days and maps them to report output. */
    @Override
    @Transactional(readOnly = true)
    public List<OverdueTaskDTO> getOverdueTasksReport(int days) {
        LocalDate today = LocalDate.now();
        LocalDate cutoffDate = today.minusDays(days);

        return taskRepository.findOverdueTasks(cutoffDate).stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .map(task -> new OverdueTaskDTO(
                        task.getTaskID(),
                        task.getTaskName(),
                        task.getDueDate(),
                        task.getUser() != null ? task.getUser().getFullName() : "",
                        task.getProject() != null ? task.getProject().getProjectName() : "",
                        (int) ChronoUnit.DAYS.between(task.getDueDate(), today)
                ))
                .collect(Collectors.toList());
    }
}