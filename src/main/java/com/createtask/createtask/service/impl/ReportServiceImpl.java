package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.response.UserProductivityDTO;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ReportServiceImpl implements reporting logic by aggregating data
 * without modifying any existing DB schema or data.
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;

    /**
     * TaskRepository provides the task count queries per user.
     * Reuses existing repository — no new repository needed.
     */
    private final TaskRepository taskRepository;

    public ReportServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Fetches all users, then for each user:
     *   1. Counts total tasks assigned via taskRepository
     *   2. Counts tasks with status "Completed"
     *   3. Counts tasks with status "Pending"
     *   4. Calculates completion rate as (completed / total) * 100
     *      — returns 0.0 if user has no tasks to avoid division by zero
     * Returns result sorted by userId ascending using AppUser's compareTo().
     */
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
}