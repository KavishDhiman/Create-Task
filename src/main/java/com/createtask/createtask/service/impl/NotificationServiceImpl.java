package com.createtask.createtask.service.impl;

// Input DTO from controller
import com.createtask.createtask.dto.request.NotificationRequestDTO;

// Output DTO returned to controller
import com.createtask.createtask.dto.response.NotificationResponseDTO;

// JPA entity for notification table
import com.createtask.createtask.entity.Notification;

// User entity needed to link notification with user
import com.createtask.createtask.entity.AppUser;

// Custom exception thrown when notification is not found
import com.createtask.createtask.exception.NotificationNotFoundException;

// Custom exception thrown when recipient user is not found
import com.createtask.createtask.exception.NotificationRecipientNotFoundException;

// Repository used for notification DB operations
import com.createtask.createtask.repository.NotificationRepository;

// Repository used for user DB operations
import com.createtask.createtask.repository.UserRepository;

// Service interface implemented by this class
import com.createtask.createtask.service.NotificationService;

// Marks this class as a Spring service bean
import org.springframework.stereotype.Service;

// Provides transaction management
import org.springframework.transaction.annotation.Transactional;

// Used to set current date and time
import java.time.LocalDateTime;

// Used for returning list of response DTOs
import java.util.List;

// Used to convert entity list into DTO list
import java.util.stream.Collectors;

// Registers this class as a service component in Spring container
@Service

// Makes methods transactional by default
@Transactional
public class NotificationServiceImpl implements NotificationService {

    // Repository dependency for Notification entity
    private final NotificationRepository notificationRepository;

    // Repository dependency for User entity
    private final UserRepository userRepository;

    // Constructor injection for repositories
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UserRepository userRepository) {

        // Assign injected notification repository to field
        this.notificationRepository = notificationRepository;

        // Assign injected user repository to field
        this.userRepository = userRepository;
    }

    // Creates a new notification
    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {

        // Find user by userId from request DTO
        AppUser user = userRepository.findById(requestDTO.getUserId())

                // Throw exception if user does not exist
                .orElseThrow(() -> new NotificationRecipientNotFoundException(
                        "User with ID " + requestDTO.getUserId() + " not found"));

        // Create new Notification entity object
        Notification notification = new Notification();

        // Set manually provided notification ID from request DTO
        notification.setNotificationID(requestDTO.getNotificationID());

        // Set notification text from request DTO
        notification.setText(requestDTO.getText());

        // Set createdAt using current server time
        notification.setCreatedAt(LocalDateTime.now());

        // Link notification with the found user entity
        notification.setUser(user);

        // Save notification entity into database
        Notification saved = notificationRepository.save(notification);

        // Convert saved entity into response DTO and return it
        return new NotificationResponseDTO(

                // Return saved notification ID
                saved.getNotificationID(),

                // Return saved notification text
                saved.getText(),

                // Return saved creation timestamp
                saved.getCreatedAt(),

                // Return linked user ID
                saved.getUser().getUserID(),

                // Return linked user's full name
                saved.getUser().getFullName()
        );
    }

    // Gets one notification by notification ID
    @Override

    // Marks this method as read-only because it only fetches data
    @Transactional(readOnly = true)
    public NotificationResponseDTO getNotificationById(int notificationId) {

        // Find notification by ID
        Notification notification = notificationRepository.findById(notificationId)

                // Throw exception if notification does not exist
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification with ID " + notificationId + " not found"));

        // Convert found entity into response DTO and return it
        return new NotificationResponseDTO(

                // Return notification ID
                notification.getNotificationID(),

                // Return notification text
                notification.getText(),

                // Return notification creation timestamp
                notification.getCreatedAt(),

                // Return user ID linked to notification
                notification.getUser().getUserID(),

                // Return user's full name linked to notification
                notification.getUser().getFullName()
        );
    }

    // Gets all notifications for a specific user
    @Override

    // Marks this method as read-only because it only fetches data
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotificationsByUserId(int userId) {

        // Check whether user exists before fetching notifications
        if (!userRepository.existsById(userId)) {

            // Throw exception if user does not exist
            throw new NotificationRecipientNotFoundException(
                    "User with ID " + userId + " not found");
        }

        // Fetch notifications by user ID ordered by createdAt descending
        return notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(userId)

                // Convert List into Stream for mapping
                .stream()

                // Convert each Notification entity into NotificationResponseDTO
                .map(n -> new NotificationResponseDTO(

                        // Map notification ID
                        n.getNotificationID(),

                        // Map notification text
                        n.getText(),

                        // Map notification creation timestamp
                        n.getCreatedAt(),

                        // Map linked user ID
                        n.getUser().getUserID(),

                        // Map linked user's full name
                        n.getUser().getFullName()
                ))

                // Collect mapped DTOs into a List
                .collect(Collectors.toList());
    }

    // Deletes notification by notification ID
    @Override
    public String deleteNotification(int notificationId) {

        // Check whether notification exists before deleting
        if (!notificationRepository.existsById(notificationId)) {

            // Throw exception if notification does not exist
            throw new NotificationNotFoundException(
                    "Notification with ID " + notificationId + " not found");
        }

        // Delete notification from database by ID
        notificationRepository.deleteById(notificationId);

        // Return confirmation message
        return "Notification with ID " + notificationId + " deleted successfully";
    }
}