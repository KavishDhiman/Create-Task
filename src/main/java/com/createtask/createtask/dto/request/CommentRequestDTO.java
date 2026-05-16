package com.createtask.createtask.dto.request;

public class CommentRequestDTO {

    private int commentID;
    private String text;
    private int userID;

    public CommentRequestDTO() {
    }

    public CommentRequestDTO(int commentID,
                             String text,
                             int userID) {
        this.commentID = commentID;
        this.text = text;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}