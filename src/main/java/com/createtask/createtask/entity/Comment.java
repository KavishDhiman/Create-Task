package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Comment")
public class Comment {

    @Id
    @Column(name = "CommentID")
    private int commentID;

    @Column(name = "Text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}
