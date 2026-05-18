package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class NotificationRequestDTO {

    @NotNull(message = "Notification ID must not be null")
    @Positive(message = "Notification ID must be a positive number")
    private Integer notificationID;

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;

    @NotNull(message = "Notification text must not be null")
    @NotBlank(message = "Notification text must not be blank")
    @Size(min = 1, max = 2000, message = "Notification text must be between 1 and 2000 characters")
    private String text;

    public NotificationRequestDTO() {
    }

    public NotificationRequestDTO(Integer notificationID, Integer userId, String text) {
        this.notificationID = notificationID;
        this.userId = userId;
        this.text = text;
    }

    public Integer getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setText(String text) {
        this.text = text;
    }
}