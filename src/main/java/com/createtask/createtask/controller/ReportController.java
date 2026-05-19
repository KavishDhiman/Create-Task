package com.createtask.createtask.controller;

import com.createtask.createtask.dto.response.ProjectSummaryDTO;
import com.createtask.createtask.dto.response.UserProductivityDTO;
import com.createtask.createtask.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Exposes all reporting endpoints — kept separate from business CRUD controllers.
// Both endpoints are read-only and return aggregated data for dashboard use.
@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "High-impact reporting endpoints for business insights")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Returns task completion statistics per user — totalTasks, completedTasks,
    // pendingTasks and completionRate — sorted by userId ascending.
    @Operation(
            summary = "User Productivity Report",
            description = "Returns task completion statistics per user — totalTasks, completedTasks, pendingTasks, completionRate(%)"
    )
    @ApiResponse(responseCode = "200", description = "Productivity report generated successfully")
    @GetMapping("/users/productivity")
    public ResponseEntity<List<UserProductivityDTO>> getUserProductivityReport() {
        return ResponseEntity.ok(reportService.getUserProductivityReport());
    }

    // Returns a health summary per project showing task breakdown and how far
    // along each project is — simulates a real company project dashboard.
    @Operation(
            summary = "Project Summary Dashboard",
            description = "Returns task breakdown per project — totalTasks, completedTasks, inProgressTasks, pendingTasks, completionPercentage(%)"
    )
    @ApiResponse(responseCode = "200", description = "Project summary report generated successfully")
    @GetMapping("/projects/summary")
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectSummaryReport() {
        return ResponseEntity.ok(reportService.getProjectSummaryReport());
    }
}