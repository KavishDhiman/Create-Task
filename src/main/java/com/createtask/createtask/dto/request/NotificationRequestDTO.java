package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank; // Ensures string is not null, not empty, and not whitespace-only
import jakarta.validation.constraints.NotNull; // Ensures field value is not null
import jakarta.validation.constraints.Positive; // Ensures numeric value is strictly greater than zero
import jakarta.validation.constraints.Size; // Restricts the minimum and/or maximum length of a string

// DTO used when the client sends a request to CREATE a new notification
// Keeps the API payload clean — never exposes internal entity fields directly
public class NotificationRequestDTO {

    @NotNull(message = "User ID must not be null") // Client must always provide a recipient user ID
    @Positive(message = "User ID must be a positive number") // Prevents zero or negative IDs from passing through
    private Integer userId; // ID of the user who will receive this notification

    @NotNull(message = "Notification text must not be null") // Explicitly rejects null before @NotBlank check
    @NotBlank(message = "Notification text must not be blank") // Rejects empty strings and whitespace-only values
    @Size(min = 1, max = 2000, message = "Notification text must be between 1 and 2000 characters") // Guards both ends of the length range
    private String text; // The notification message body displayed to the user

    // No-args constructor — required by Jackson to deserialize incoming JSON into this object
    public NotificationRequestDTO() {
    }

    // All-args constructor — useful for constructing the DTO manually in tests or internal calls
    public NotificationRequestDTO(Integer userId, String text) {
        this.userId = userId; // Assigns the recipient user ID
        this.text = text; // Assigns the notification message body
    }

    // Returns the recipient user's ID
    public Integer getUserId() {
        return userId; // Used by the service to look up the User entity
    }

    // Returns the notification message text
    public String getText() {
        return text; // The message body to be stored in the Notification entity
    }

    // Sets the recipient user ID
    public void setUserId(Integer userId) {
        this.userId = userId; // Allows Jackson to bind the JSON field "userId" to this field
    }

    // Sets the notification message text
    public void setText(String text) {
        this.text = text; // Allows Jackson to bind the JSON field "text" to this field
    }
}