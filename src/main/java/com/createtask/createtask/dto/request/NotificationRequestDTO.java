package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank; // Ensures string is not empty or whitespace-only
import jakarta.validation.constraints.NotNull; // Ensures field is not null
import jakarta.validation.constraints.Positive; // Ensures numeric value is greater than zero
import jakarta.validation.constraints.Size; // Restricts string length

// DTO used for receiving notification creation requests from the client
// Follows DTO Pattern by separating API request data from the Entity layer
public class NotificationRequestDTO {

    // Unique identifier for the notification
    // Validation ensures only positive numeric IDs are accepted
    @NotNull(message = "Notification ID must not be null")
    @Positive(message = "Notification ID must be a positive number")
    private Integer notificationID;

    // Stores the ID of the user receiving the notification
    // Prevents invalid or negative user IDs
    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;

    // Stores the notification message text
    // Validation prevents null, blank, or excessively large content
    @NotNull(message = "Notification text must not be null")
    @NotBlank(message = "Notification text must not be blank")
    @Size(min = 1, max = 2000, message = "Notification text must not exceed 2000 characters")
    private String text;

    // Default constructor required by frameworks like Spring and Jackson
    public NotificationRequestDTO() {
    }

    // Parameterized constructor for easier object creation and testing
    public NotificationRequestDTO(Integer notificationID, Integer userId, String text) {
        this.notificationID = notificationID;
        this.userId = userId;
        this.text = text;
    }

    // Returns notification ID
    public Integer getNotificationID() {
        return notificationID;
    }

    // Sets notification ID
    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    // Returns user ID
    public Integer getUserId() {
        return userId;
    }

    // Returns notification text
    public String getText() {
        return text;
    }

    // Sets user ID
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // Sets notification text
    public void setText(String text) {
        this.text = text;
    }
}