package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a comment associated with a task and user.
 */
@Entity
@Table(name = "Comment")
public class Comment implements Comparable<Comment> {

    /**
     * Unique identifier for the comment.
     */
    @Id
    @Column(name = "CommentID")
    private int commentID;

    /**
     * Text content of the comment.
     */
    @NotBlank(message = "Comment text is required")
    @Column(name = "Text", columnDefinition = "TEXT")
    private String text;

    /**
     * Timestamp indicating when the comment was created.
     */
    @NotNull(message = "Created date is required")
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    /**
     * Task associated with the comment.
     */
    @NotNull(message = "Task ID is required")
    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;

    /**
     * User associated with the comment.
     */
    @NotNull(message = "User ID is required")
    @ManyToOne
    @JoinColumn(name = "UserID")
    private AppUser user;

    /**
     * Default constructor.
     */
    public Comment() {
    }

    /**
     * Parameterized constructor for initializing comment entity fields.
     *
     * @param commentID unique comment ID
     * @param text      comment text
     * @param createdAt comment creation timestamp
     * @param task      associated task entity
     * @param user      associated user entity
     */
    public Comment(int commentID, String text, LocalDateTime createdAt, Task task, AppUser user) {
        this.commentID = commentID;
        this.text = text;
        this.createdAt = createdAt;
        this.task = task;
        this.user = user;
    }

    /**
     * Retrieves the comment ID.
     *
     * @return comment ID
     */
    public int getCommentID() {
        return commentID;
    }

    /**
     * Sets the comment ID.
     *
     * @param commentID comment ID to set
     */
    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    /**
     * Retrieves the comment text.
     *
     * @return comment text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the comment text.
     *
     * @param text comment text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Retrieves the comment creation timestamp.
     *
     * @return comment creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the comment creation timestamp.
     *
     * @param createdAt timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Retrieves the associated task.
     *
     * @return associated task entity
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the associated task.
     *
     * @param task task entity to set
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Retrieves the associated user.
     *
     * @return associated user entity
     */
    public AppUser getUser() {
        return user;
    }

    /**
     * Sets the associated user.
     *
     * @param user user entity to set
     */
    public void setUser(AppUser user) {
        this.user = user;
    }

    /**
     * Compares two comment objects based on comment ID.
     *
     * @param o object to compare
     * @return true if both objects have the same comment ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return commentID == comment.commentID;
    }

    /**
     * Generates hash code using comment ID.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(commentID);
    }

    /**
     * Compares comments based on creation timestamp.
     *
     * @param other another comment object
     * @return comparison result based on creation time
     */
    @Override
    public int compareTo(Comment other) {
        return this.createdAt.compareTo(other.createdAt);
    }
}