package com.createtask.createtask.controller;

import com.createtask.createtask.dto.response.OverdueTaskDTO;
import com.createtask.createtask.dto.response.ProjectSummaryDTO;
import com.createtask.createtask.dto.response.UserProductivityDTO;
import com.createtask.createtask.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "High-impact reporting endpoints for business insights")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "User Productivity Report",
            description = "Returns task completion statistics per user — totalTasks, completedTasks, pendingTasks, completionRate(%)"
    )
    @ApiResponse(responseCode = "200", description = "Productivity report generated successfully")
    @GetMapping("/users/productivity")
    public ResponseEntity<List<UserProductivityDTO>> getUserProductivityReport() {
        return ResponseEntity.ok(reportService.getUserProductivityReport());
    }

    @Operation(
            summary = "Project Summary Dashboard",
            description = "Returns task breakdown per project — totalTasks, completedTasks, inProgressTasks, pendingTasks, completionPercentage(%)"
    )
    @ApiResponse(responseCode = "200", description = "Project summary report generated successfully")
    @GetMapping("/projects/summary")
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectSummaryReport() {
        return ResponseEntity.ok(reportService.getProjectSummaryReport());
    }

    @Operation(
            summary = "Overdue Tasks Report",
            description = "Returns tasks that are overdue beyond the requested number of days"
    )
    @ApiResponse(responseCode = "200", description = "Overdue tasks report generated successfully")
    @GetMapping("/tasks/overdue")
    public ResponseEntity<List<OverdueTaskDTO>> getOverdueTasksReport(
            @RequestParam int days) {

        return ResponseEntity.ok(reportService.getOverdueTasksReport(days));
    }
}