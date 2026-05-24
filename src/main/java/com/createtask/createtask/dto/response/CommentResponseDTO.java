package com.createtask.createtask.dto.response;

import java.time.LocalDateTime;

/**
 * DTO used for sending comment response data.
 */
public class CommentResponseDTO {

    /**
     * Unique identifier for the comment.
     */
    private int commentID;

    /**
     * Text content of the comment.
     */
    private String text;

    /**
     * Timestamp indicating when the comment was created.
     */
    private LocalDateTime createdAt;

    /**
     * ID of the associated task.
     */
    private int taskID;

    /**
     * ID of the user who created the comment.
     */
    private int userID;

    /**
     * Default constructor.
     */
    public CommentResponseDTO() {
    }

    /**
     * Parameterized constructor for initializing comment response data.
     *
     * @param commentID unique comment ID
     * @param text      comment text
     * @param createdAt comment creation timestamp
     * @param taskID    associated task ID
     * @param userID    associated user ID
     */
    public CommentResponseDTO(int commentID, String text, LocalDateTime createdAt,
                              int taskID, int userID) {
        this.commentID = commentID;
        this.text = text;
        this.createdAt = createdAt;
        this.taskID = taskID;
        this.userID = userID;
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
     * Retrieves the task ID.
     *
     * @return task ID
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * Sets the task ID.
     *
     * @param taskID task ID to set
     */
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    /**
     * Retrieves the user ID.
     *
     * @return user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID.
     *
     * @param userID user ID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}