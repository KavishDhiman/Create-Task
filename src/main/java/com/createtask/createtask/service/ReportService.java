package com.createtask.createtask.service;

import com.createtask.createtask.dto.response.OverdueTaskDTO;
import com.createtask.createtask.dto.response.ProjectSummaryDTO;
import com.createtask.createtask.dto.response.UserProductivityDTO;

import java.util.List;

// Service interface for report-related business operations
public interface ReportService {

    // Generates productivity report for all users
    List<UserProductivityDTO> getUserProductivityReport();

    // Generates project summary report for all projects
    List<ProjectSummaryDTO> getProjectSummaryReport();

    // Generates overdue tasks report based on days input
    List<OverdueTaskDTO> getOverdueTasksReport(int days);
}