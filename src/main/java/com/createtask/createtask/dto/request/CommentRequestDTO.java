package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO used for receiving comment creation request data.
 */
public class CommentRequestDTO {

    /**
     * Unique identifier for the comment.
     */
    private int commentID;

    /**
     * Text content of the comment.
     */
    @NotBlank(message = "Comment text is required")
    private String text;

    /**
     * ID of the user who created the comment.
     */
    private int userID;

    /**
     * Default constructor.
     */
    public CommentRequestDTO() {
    }

    /**
     * Parameterized constructor for initializing comment request data.
     *
     * @param commentID unique comment ID
     * @param text      comment text
     * @param userID    ID of the user creating the comment
     */
    public CommentRequestDTO(int commentID, String text, int userID) {
        this.commentID = commentID;
        this.text = text;
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