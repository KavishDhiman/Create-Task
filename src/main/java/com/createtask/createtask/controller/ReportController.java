package com.createtask.createtask.controller;

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

/**
 * ReportController exposes all high-impact reporting endpoints.
 */
@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "High-impact reporting endpoints for business insights")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Returns a productivity report for all users showing total, completed,
     * pending task counts and completion rate percentage.
     * Results are sorted by userId ascending.
     */
    @Operation(
            summary = "User Productivity Report",
            description = "Returns task completion statistics per user — totalTasks, completedTasks, pendingTasks, completionRate(%)"
    )
    @ApiResponse(responseCode = "200", description = "Productivity report generated successfully")
    @GetMapping("/users/productivity")
    public ResponseEntity<List<UserProductivityDTO>> getUserProductivityReport() {
        return ResponseEntity.ok(reportService.getUserProductivityReport());
    }
}