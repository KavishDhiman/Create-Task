package com.createtask.createtask.dto.response;

import java.time.LocalDateTime;

public class CommentResponseDTO {

    private int commentID;
    private String text;
    private LocalDateTime createdAt;
    private int taskID;
    private int userID;

    public CommentResponseDTO() {
    }

    public CommentResponseDTO(int commentID, String text, LocalDateTime createdAt,
                              int taskID, int userID) {
        this.commentID = commentID;
        this.text = text;
        this.createdAt = createdAt;
        this.taskID = taskID;
        this.userID = userID;
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

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}