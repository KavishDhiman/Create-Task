package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.NotificationRequestDTO; // Input DTO for creating notification
import com.createtask.createtask.dto.response.NotificationResponseDTO; // Output DTO returned to client
import com.createtask.createtask.service.NotificationService; // Service interface for notification logic
import io.swagger.v3.oas.annotations.Operation; // Swagger documentation for endpoint summary
import io.swagger.v3.oas.annotations.Parameter; // Swagger documentation for path variable description
import io.swagger.v3.oas.annotations.tags.Tag; // Swagger: groups all endpoints under a named tag in UI
import jakarta.validation.Valid; // Triggers bean validation on the incoming request body
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus; // Provides HTTP status code constants
import org.springframework.http.ResponseEntity; // Wraps response body with a status code
import org.springframework.web.bind.annotation.*; // Imports all REST mapping annotations

import java.util.List; // Return type for list endpoints

@RestController // Marks this class as a REST controller — responses are JSON by default
@RequestMapping("/api/v1") // Base URL prefix applied to all endpoints in this controller
@Tag(name = "Notification", description = "APIs for creating, retrieving, and managing user notifications") // Swagger grouping label
public class NotificationController {

    private final NotificationService notificationService; // Service dependency — coded to the interface

    // Constructor injection — Spring detects the single constructor and auto-wires NotificationService
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService; // Assigns the injected service to the final field
    }

    // Creates a new notification
    @Operation(summary = "Create notification") // Swagger endpoint label
    @PostMapping("/notifications") // Handles POST /api/v1/notifications
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationRequestDTO requestDTO) { // @Valid triggers all DTO field validations

        NotificationResponseDTO response = notificationService.createNotification(requestDTO); // Delegate to service
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created with saved notification body
    }

    // Gets notification by notification ID
    @Operation(summary = "Get notification by ID") // Swagger endpoint label
    @GetMapping("/notifications/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotification(
            @PathVariable @Positive( message = "Notification ID must be a positive number")
            int notificationId) { // Extracts notificationId from the URL path segment

        NotificationResponseDTO response = notificationService.getNotificationById(notificationId); // Service call
        return ResponseEntity.ok(response); // 200 OK with the notification body
    }

    // Gets all notifications of a user
    @Operation(summary = "Get notifications by user ID") // Swagger endpoint label
    @GetMapping("/users/{userId}/notifications") // Handles GET /api/v1/users/{userId}/notifications
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(
            @Parameter(description = "User ID") // Swagger description for the path variable
            @PathVariable @Positive( message = "User ID must be a positive number")  int userId) { // Extracts userId from the URL path segment

        List<NotificationResponseDTO> notifications =
                notificationService.getNotificationsByUserId(userId); // Service call

        return ResponseEntity.ok(notifications); // 200 OK with list — may be empty if user has no notifications
    }

    // Deletes notification by notification ID — returns confirmation message from the service
    @Operation(summary = "Delete notification") // Swagger endpoint label
    @DeleteMapping("/notifications/{notificationId}") // Handles DELETE /api/v1/notifications/{notificationId}
    public ResponseEntity<String> deleteNotification(
            @Parameter(description = "Notification ID") // Swagger description for the path variable
            @PathVariable @Positive( message = "Notification ID must be a positive number")  int notificationId) { // Extracts notificationId from the URL path segment

        String message = notificationService.deleteNotification(notificationId); // Service deletes and returns confirmation
        return ResponseEntity.ok(message); // 200 OK with the deletion confirmation string
    }
}