package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.response.ProjectSummaryDTO;
import com.createtask.createtask.dto.response.UserProductivityDTO;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.entity.Project;
import com.createtask.createtask.repository.ProjectRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Implements all reporting logic by aggregating existing data — no schema changes needed.
// Both reports reuse repositories already present in the project.
@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    // Added ProjectRepository to support the new project summary report.
    private final ProjectRepository projectRepository;

    public ReportServiceImpl(UserRepository userRepository,
                             TaskRepository taskRepository,
                             ProjectRepository projectRepository) {
        this.userRepository    = userRepository;
        this.taskRepository    = taskRepository;
        this.projectRepository = projectRepository;
    }

    // Fetches all users, aggregates their task counts, sorts by userID ascending
    // using AppUser's compareTo() — same logic as before, untouched.
    @Override
    @Transactional(readOnly = true)
    public List<UserProductivityDTO> getUserProductivityReport() {
        List<AppUser> users = userRepository.findAll();

        return users.stream()
                .sorted()
                .map(user -> {
                    long total     = taskRepository.countByUserID(user.getUserID());
                    long completed = taskRepository.countByUserIDAndStatus(user.getUserID(), "Completed");
                    long pending   = taskRepository.countByUserIDAndStatus(user.getUserID(), "Pending");

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

    // Fetches all projects, then for each project counts tasks by status
    // and calculates completion percentage — sorted by projectID ascending.
    @Override
    @Transactional(readOnly = true)
    public List<ProjectSummaryDTO> getProjectSummaryReport() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .sorted()   // uses Project's compareTo() which sorts by startDate
                .map(project -> {
                    int id = project.getProjectID();

                    long total      = projectRepository.countTasksByProjectID(id);
                    long completed  = projectRepository.countTasksByProjectIDAndStatus(id, "Completed");
                    long inProgress = projectRepository.countTasksByProjectIDAndStatus(id, "In Progress");
                    long pending    = projectRepository.countTasksByProjectIDAndStatus(id, "Pending");

                    // Rounds to 2 decimal places — returns 0.0 if project has no tasks yet.
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
}