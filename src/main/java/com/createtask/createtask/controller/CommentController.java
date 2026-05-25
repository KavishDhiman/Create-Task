package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;
import com.createtask.createtask.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Returns HTTP 201 Created on successful creation.
     *
     * @param taskId     ID of the task to which the comment belongs
     * @param requestDTO request payload containing comment details
     * @return 201 Created with the saved comment response details
     */
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable int taskId,
            @Valid @RequestBody CommentRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.addComment(taskId, requestDTO));
    }

    /**
     * Retrieves all comments associated with a specific task.
     * Returns HTTP 200 OK with the list of comments.
     *
     * @param taskId ID of the task
     * @return 200 OK with list of comment response objects
     */
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByTaskId(
            @PathVariable int taskId) {

        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }

    /**
     * Deletes a comment using its unique comment ID.
     * Returns HTTP 200 OK with a confirmation message on success.
     *
     * @param commentId ID of the comment to delete
     * @return 200 OK with success message after deletion
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {

        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}