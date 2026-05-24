package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;
import com.createtask.createtask.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for managing comment-related APIs.
 * Provides endpoints for adding, retrieving, and deleting comments.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Comment Management", description = "APIs for managing comments")
public class CommentController {

    /**
     * Service layer dependency for comment operations.
     */
    private final CommentService commentService;

    /**
     * Constructor-based dependency injection for CommentService.
     *
     * @param commentService service used for comment operations
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Adds a new comment for a specific task.
     *
     * @param taskId     ID of the task to which the comment belongs
     * @param requestDTO request payload containing comment details
     * @return saved comment response details
     */
    @PostMapping("/tasks/{taskId}/comments")
    public CommentResponseDTO addComment(
            @PathVariable int taskId,
            @Valid @RequestBody CommentRequestDTO requestDTO) {

        return commentService.addComment(taskId, requestDTO);
    }

    /**
     * Retrieves all comments associated with a specific task.
     *
     * @param taskId ID of the task
     * @return list of comment response objects
     */
    @GetMapping("/tasks/{taskId}/comments")
    public List<CommentResponseDTO> getCommentsByTaskId(
            @PathVariable int taskId) {

        return commentService.getCommentsByTaskId(taskId);
    }

    /**
     * Deletes a comment using its unique comment ID.
     *
     * @param commentId ID of the comment to delete
     * @return success message after deletion
     */
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable int commentId) {

        return commentService.deleteComment(commentId);
    }
}