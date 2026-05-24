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

@RestController // Marks this class as REST controller
@RequestMapping("/api/v1/reports") // Base URL mapping for report APIs
@Tag(name = "Reports", description = "High-impact reporting endpoints for business insights") // Swagger API documentation tag
public class ReportController {

    private final ReportService reportService; // Service dependency for report operations

    // Constructor injection for ReportService
    public ReportController(ReportService reportService) {

        this.reportService = reportService; // Assigns ReportService object
    }

    @Operation( // Swagger operation details
            summary = "User Productivity Report",
            description = "Returns task completion statistics per user — totalTasks, completedTasks, pendingTasks, completionRate(%)"
    )
    @ApiResponse(responseCode = "200", description = "Productivity report generated successfully") // Swagger response documentation
    @GetMapping("/users/productivity") // Maps GET request
    public ResponseEntity<List<UserProductivityDTO>> getUserProductivityReport() {

        return ResponseEntity.ok( // Returns success response
                reportService.getUserProductivityReport()
        );
    }

    @Operation( // Swagger operation details
            summary = "Project Summary Dashboard",
            description = "Returns task breakdown per project — totalTasks, completedTasks, inProgressTasks, pendingTasks, completionPercentage(%)"
    )
    @ApiResponse(responseCode = "200", description = "Project summary report generated successfully") // Swagger response documentation
    @GetMapping("/projects/summary") // Maps GET request
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectSummaryReport() {

        return ResponseEntity.ok( // Returns success response
                reportService.getProjectSummaryReport()
        );
    }

    @Operation( // Swagger operation details
            summary = "Overdue Tasks Report",
            description = "Returns tasks that are overdue beyond the requested number of days"
    )
    @ApiResponse(responseCode = "200", description = "Overdue tasks report generated successfully") // Swagger response documentation
    @GetMapping("/tasks/overdue") // Maps GET request
    public ResponseEntity<List<OverdueTaskDTO>> getOverdueTasksReport(

            @RequestParam(defaultValue = "7") int days) { // Receives days parameter from request

        return ResponseEntity.ok( // Returns success response
                reportService.getOverdueTasksReport(days)
        );
    }
}