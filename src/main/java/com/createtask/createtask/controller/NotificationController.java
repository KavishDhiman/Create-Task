package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.NotificationRequestDTO; // Input DTO for creating notification
import com.createtask.createtask.dto.response.NotificationResponseDTO; // Output DTO returned to client
import com.createtask.createtask.service.NotificationService; // Service interface for notification logic
import io.swagger.v3.oas.annotations.Operation; // Swagger documentation for endpoint summary
import io.swagger.v3.oas.annotations.Parameter; // Swagger documentation for path variable
import jakarta.validation.Valid; // Enables validation for request body
import org.springframework.http.HttpStatus; // Provides HTTP status codes
import org.springframework.http.ResponseEntity; // Wraps response body with status code
import org.springframework.web.bind.annotation.*; // REST controller annotations

import java.util.List; // Used for returning list of notifications

@RestController // Marks this class as REST controller
@RequestMapping("/api/v1") // Base URL for all notification APIs
public class NotificationController {

    private final NotificationService notificationService; // Service dependency

    // Constructor injection for NotificationService
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Creates a new notification
    @Operation(summary = "Create notification")
    @PostMapping("/notifications")
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationRequestDTO requestDTO) {

        NotificationResponseDTO response = notificationService.createNotification(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Gets notification by notification ID
    @Operation(summary = "Get notification by ID")
    @GetMapping("/notifications/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotification(
            @Parameter(description = "Notification ID")
            @PathVariable int notificationId) {

        NotificationResponseDTO response = notificationService.getNotificationById(notificationId);
        return ResponseEntity.ok(response);
    }

    // Gets all notifications of a user
    @Operation(summary = "Get notifications by user ID")
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(
            @Parameter(description = "User ID")
            @PathVariable int userId) {

        List<NotificationResponseDTO> notifications =
                notificationService.getNotificationsByUserId(userId);

        return ResponseEntity.ok(notifications);
    }

    // Deletes notification by notification ID
    @Operation(summary = "Delete notification")
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "Notification ID")
            @PathVariable int notificationId) {

        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}