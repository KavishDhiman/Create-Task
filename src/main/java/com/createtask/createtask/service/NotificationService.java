package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.NotificationRequestDTO; // Input DTO for creating notifications
import com.createtask.createtask.dto.response.NotificationResponseDTO; // Output DTO returned to controllers

import java.util.List; // For returning lists of notifications

// Interface defines the contract for notification business logic
// The controller only depends on this interface — not the implementation (Dependency Inversion Principle)
public interface NotificationService {

    // Create and persist a new notification for a user
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO);

    // Fetch a single notification by its ID — throws NotificationNotFoundException if missing
    NotificationResponseDTO getNotificationById(int notificationId);

    // Fetch all notifications for a specific user, ordered by newest first
    List<NotificationResponseDTO> getNotificationsByUserId(int userId);

    // Delete a notification by ID — throws NotificationNotFoundException if not found
    void deleteNotification(int notificationId);
}