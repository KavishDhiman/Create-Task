package com.createtask.createtask.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Notification")
public class Notification {

    @Id
    @Column(name = "NotificationID")
    private int notificationID;

    @Column(name = "Text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}
