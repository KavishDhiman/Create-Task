package com.createtask.createtask.entity;

import jakarta.persistence.*; // JPA annotations for entity mapping
import jakarta.validation.constraints.NotBlank; // Ensures string fields are not null, empty, or whitespace
import jakarta.validation.constraints.NotNull; // Ensures fields are not null
import jakarta.validation.constraints.Size; // Restricts the max/min length of string fields
import java.time.LocalDateTime; // For storing creation timestamps
import java.util.Objects; // For null-safe equals and hashCode computation

@Entity // Marks this class as a JPA-managed persistent entity
@Table(name = "Notification") // Maps this entity to the "Notification" table in the database
public class Notification {

    @Id // Marks this field as the primary key
    @Column(name = "NotificationID") // Maps to the NotificationID column in the DB
    private int notificationID; // Unique identifier for each notification record

    @NotBlank(message = "Notification text must not be blank") // Rejects null, empty string, or whitespace-only values
    @Size(max = 2000, message = "Notification text must not exceed 2000 characters") // Prevents oversized messages from being stored
    @Column(name = "Text", columnDefinition = "TEXT", nullable = false) // Stored as TEXT in DB; column cannot be null
    private String text; // The actual notification message content shown to the user

    @NotNull(message = "Created date must not be null") // createdAt must always be set before persisting
    @Column(name = "CreatedAt", nullable = false) // Maps to CreatedAt column; DB also enforces NOT NULL
    private LocalDateTime createdAt; // Timestamp recording when the notification was created

    @NotNull(message = "User ID must not be null") // Every notification must be linked to a recipient user
    @ManyToOne // Many notifications can belong to one user
    @JoinColumn(name = "UserID", nullable = false) // Foreign key column linking to the User table; enforced at DB level
    private AppUser user; // The User entity who is the recipient of this notification

    // Returns the unique notification ID (primary key)
    public int getNotificationID() {
        return notificationID; // Returns the auto-generated primary key value
    }

    // Returns the notification message text
    public String getText() {
        return text; // Returns the content of the notification message
    }

    // Returns the timestamp when this notification was created
    public LocalDateTime getCreatedAt() {
        return createdAt; // Returns the LocalDateTime of creation
    }

    // Returns the recipient user of this notification
    public AppUser getUser() {
        return user; // Returns the User entity linked to this notification
    }

    // Sets the unique notification ID
    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    // Sets the notification message text
    public void setText(String text) {
        this.text = text;
    }

    // Sets the creation timestamp of the notification
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt; // Assigns when this notification was created
    }

    // Sets the recipient user of this notification
    public void setUser(AppUser user) {
        this.user = user; // Assigns the User entity linked to this notification
    }

    // Two Notification objects are equal if and only if they share the same notificationID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same object reference — trivially equal
        if (o == null || getClass() != o.getClass()) return false; // Null or different type — not equal
        Notification that = (Notification) o; // Safe cast after type check
        return notificationID == that.notificationID; // Only the primary key determines equality
    }

    // hashCode must use the same field as equals() so Sets/Maps work correctly
    @Override
    public int hashCode() {
        return Objects.hash(notificationID); // Generates a stable hash based on the primary key
    }
}