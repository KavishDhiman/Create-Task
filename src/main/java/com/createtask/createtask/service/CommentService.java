package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;

import java.util.List;

/**
 * Service interface defining business operations for comment management.
 */
public interface CommentService {

    /**
     * Adds a new comment for a specific task.
     *
     * @param taskId     ID of the task to which the comment belongs
     * @param requestDTO request payload containing comment details
     * @return saved comment response object
     */
    CommentResponseDTO addComment(int taskId,
                                  CommentRequestDTO requestDTO);

    /**
     * Retrieves all comments associated with a specific task.
     *
     * @param taskId ID of the task
     * @return list of comment response objects
     */
    List<CommentResponseDTO> getCommentsByTaskId(int taskId);

    /**
     * Deletes a comment using its unique comment ID.
     *
     * @param commentId ID of the comment to delete
     * @return success message after deletion
     */
    String deleteComment(int commentId);
}