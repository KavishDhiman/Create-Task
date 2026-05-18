package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;

import java.util.List;

public interface CommentService {

    // Adds a comment for a task
    CommentResponseDTO addComment(int taskId,
                                  CommentRequestDTO requestDTO);

    // Retrieves all comments under a task sorted by comment ID
    List<CommentResponseDTO> getCommentsByTaskId(int taskId);

    // Deletes a comment using comment ID
    String deleteComment(int commentId);
}