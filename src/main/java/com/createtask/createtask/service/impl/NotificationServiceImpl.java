package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.NotificationRequestDTO; // Input DTO from controller
import com.createtask.createtask.dto.response.NotificationResponseDTO; // Output DTO returned to controller
import com.createtask.createtask.entity.Notification; // JPA entity for DB operations
import com.createtask.createtask.entity.User; // User entity needed for recipient lookup
import com.createtask.createtask.exception.NotificationNotFoundException; // Thrown when notification ID is missing
import com.createtask.createtask.exception.NotificationRecipientNotFoundException; // Thrown when user is not found
import com.createtask.createtask.repository.NotificationRepository; // Data access for notifications
import com.createtask.createtask.repository.UserRepository; // Data access for users
import com.createtask.createtask.service.NotificationService; // Interface this class implements
import org.springframework.stereotype.Service; // Marks this as a Spring service bean
import org.springframework.transaction.annotation.Transactional; // Handles transaction management

import java.time.LocalDateTime; // Used to set createdAt automatically
import java.util.List; // Used for returning list of notifications
import java.util.stream.Collectors; // Used to convert entity list to DTO list

@Service // Registers this class as a Spring-managed service bean
@Transactional // Runs service methods inside a transaction
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository; // Repository for notification database operations
    private final UserRepository userRepository; // Repository for user database operations

    // Constructor injection for required repositories
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    // Creates a new notification
    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new NotificationRecipientNotFoundException(
                        "User with ID " + requestDTO.getUserId() + " not found"));

        Notification notification = new Notification();

        // Since NotificationID is not AUTO_INCREMENT in the given DB schema,
        // we manually assign the next ID without changing the table.
        int nextNotificationId = (int) notificationRepository.count() + 1;
        notification.setNotificationID(nextNotificationId);

        notification.setText(requestDTO.getText());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUser(user);

        Notification savedNotification = notificationRepository.save(notification);

        return toResponseDTO(savedNotification);
    }

    // Gets notification by notification ID
    @Override
    @Transactional(readOnly = true)
    public NotificationResponseDTO getNotificationById(int notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification with ID " + notificationId + " not found"));

        return toResponseDTO(notification);
    }

    // Gets all notifications for a particular user
    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotificationsByUserId(int userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotificationRecipientNotFoundException(
                    "User with ID " + userId + " not found");
        }

        return notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Deletes notification by notification ID
    @Override
    public void deleteNotification(int notificationId) {

        if (!notificationRepository.existsById(notificationId)) {
            throw new NotificationNotFoundException(
                    "Notification with ID " + notificationId + " not found");
        }

        notificationRepository.deleteById(notificationId);
    }

    // Converts Notification entity to NotificationResponseDTO
    private NotificationResponseDTO toResponseDTO(Notification notification) {

        return new NotificationResponseDTO(
                notification.getNotificationID(),
                notification.getText(),
                notification.getCreatedAt(),
                notification.getUser().getUserID(),
                notification.getUser().getFullName()
        );
    }
}