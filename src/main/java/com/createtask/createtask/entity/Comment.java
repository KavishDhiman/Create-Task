package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Comment")
public class Comment implements Comparable<Comment> {

    @Id
    @Column(name = "CommentID")
    private int commentID;

    @NotBlank(message = "Comment text is required")
    @Column(name = "Text", columnDefinition = "TEXT")
    private String text;

    @NotNull(message = "Created date is required")
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @NotNull(message = "Task ID is required")
    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;

    @NotNull(message = "User ID is required")
    @ManyToOne
    @JoinColumn(name = "UserID")
    private AppUser user;

    public Comment() {
    }

    public Comment(int commentID, String text, LocalDateTime createdAt, Task task, AppUser user) {
        this.commentID = commentID;
        this.text = text;
        this.createdAt = createdAt;
        this.task = task;
        this.user = user;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return commentID == comment.commentID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentID);
    }

    @Override
    public int compareTo(Comment other) {
        return this.createdAt.compareTo(other.createdAt);
    }
}