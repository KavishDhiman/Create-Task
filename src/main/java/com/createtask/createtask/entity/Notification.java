package com.createtask.createtask.entity;

import jakarta.persistence.*; // JPA annotations for entity mapping
import jakarta.validation.constraints.NotBlank; // Ensures string fields are not empty or whitespace-only
import jakarta.validation.constraints.NotNull; // Ensures required fields are not null
import jakarta.validation.constraints.Size; // Restricts maximum and minimum string length
import java.time.LocalDateTime; // Used for storing notification creation timestamp
import java.util.Objects; // Used for equals() and hashCode() implementations

// Represents the Notification entity stored in the database
// Follows Entity Design Pattern by mapping Java objects to relational database tables
@Entity
@Table(name = "Notification")
public class Notification {

    // Primary key for the Notification table
    // Stores the unique identifier for each notification
    @Id
    @Column(name = "NotificationID")
    private int notificationID;

    // Stores the notification message content
    // Validation prevents blank or excessively large messages
    @NotBlank(message = "Notification text must not be blank")
    @Size(max = 2000, message = "Notification text must not exceed 2000 characters")
    @Column(name = "Text", columnDefinition = "TEXT", nullable = false)
    private String text;

    // Stores the date and time when the notification was created
    // Cannot be null because every notification must have a creation timestamp
    @NotNull(message = "Created date must not be null")
    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    // Many notifications can belong to one user
    // Establishes a Many-to-One relationship with the AppUser entity
    @NotNull(message = "User ID must not be null")
    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private AppUser user;

    // Returns the notification ID
    public int getNotificationID() {
        return notificationID;
    }

    // Returns the notification message text
    public String getText() {
        return text;
    }

    // Returns the notification creation timestamp
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Returns the user associated with this notification
    public AppUser getUser() {
        return user;
    }

    // Sets the notification ID
    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    // Sets the notification message text
    public void setText(String text) {
        this.text = text;
    }

    // Sets the notification creation timestamp
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Sets the user associated with this notification
    public void setUser(AppUser user) {
        this.user = user;
    }

    // Two Notification objects are considered equal
    // if they have the same notificationID
    @Override
    public boolean equals(Object o) {

        // Checks if both references point to the same object
        if (this == o) return true;

        // Prevents comparison with null or different class types
        if (o == null || getClass() != o.getClass()) return false;

        // Type casting after validation
        Notification that = (Notification) o;

        // Equality based only on primary key
        return notificationID == that.notificationID;
    }

    // Generates hash code using notificationID
    // Required for proper behavior in HashSet and HashMap collections
    @Override
    public int hashCode() {
        return Objects.hash(notificationID);
    }
}