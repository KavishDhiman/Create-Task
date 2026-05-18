package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;
import com.createtask.createtask.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Comment Management", description = "APIs for managing comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Adds a comment for a task
    @PostMapping("/tasks/{taskId}/comments")
    public CommentResponseDTO addComment(
            @PathVariable int taskId,
            @RequestBody CommentRequestDTO requestDTO) {

        return commentService.addComment(taskId, requestDTO);
    }

    // Retrieves all comments under a task sorted by comment ID
    @GetMapping("/tasks/{taskId}/comments")
    public List<CommentResponseDTO> getCommentsByTaskId(
            @PathVariable int taskId) {

        return commentService.getCommentsByTaskId(taskId);
    }

    // Deletes a comment using comment ID
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable int commentId) {

        return commentService.deleteComment(commentId);
    }
}