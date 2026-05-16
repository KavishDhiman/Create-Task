package com.createtask.createtask.dto.response;

import java.time.LocalDateTime; // Used for notification creation timestamp

/**
 * NotificationResponseDTO is the output object returned to the client.
 * It exposes only the required notification details and hides internal entity logic.
 */
public class NotificationResponseDTO {

    private Integer notificationID; // Unique ID of the notification

    private String text; // Actual notification message

    private LocalDateTime createdAt; // Time when notification was created

    private Integer userId; // ID of the user receiving the notification

    private String userName; // Name of the user receiving the notification

    // Default constructor required by Jackson for JSON serialization/deserialization
    public NotificationResponseDTO() {}

    /** Convenience constructor used in controller/service for mapping entity to DTO */
    public NotificationResponseDTO(Integer notificationID, String text,
                                   LocalDateTime createdAt,
                                   Integer userId, String userName) {
        this.notificationID = notificationID;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userName = userName;
    }

    // Returns notification ID
    public Integer getNotificationID() {
        return notificationID;
    }

    // Sets notification ID
    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    // Returns notification message text
    public String getText() {
        return text;
    }

    // Sets notification message text
    public void setText(String text) {
        this.text = text;
    }

    // Returns notification creation timestamp
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Sets notification creation timestamp
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Returns user ID associated with notification
    public Integer getUserId() {
        return userId;
    }

    // Sets user ID associated with notification
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // Returns user name associated with notification
    public String getUserName() {
        return userName;
    }

    // Sets user name associated with notification
    public void setUserName(String userName) {
        this.userName = userName;
    }
}