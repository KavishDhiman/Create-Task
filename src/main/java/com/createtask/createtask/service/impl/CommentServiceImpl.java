package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;
import com.createtask.createtask.entity.Comment;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.CommentNotFoundException;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.repository.CommentRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              TaskRepository taskRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Adds a comment for a task
    @Override
    public CommentResponseDTO addComment(int taskId,
                                         CommentRequestDTO requestDTO) {

        // Duplicate ID check
        if (commentRepository.existsById(requestDTO.getCommentID())) {
            throw new DuplicateResourceException(
                    "Comment already exists with ID: " + requestDTO.getCommentID());
        }

        // Task existence check
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task ID not found: " + taskId);
        }

        // User existence check
        if (!userRepository.existsById(requestDTO.getUserID())) {
            throw new RuntimeException("User ID not found: " + requestDTO.getUserID());
        }

        Task task = taskRepository.findById(taskId).orElseThrow();
        AppUser user = userRepository.findById(requestDTO.getUserID()).orElseThrow();

        Comment comment = new Comment();

        comment.setCommentID(requestDTO.getCommentID());
        comment.setText(requestDTO.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setTask(task);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDTO(
                savedComment.getCommentID(),
                savedComment.getText(),
                savedComment.getCreatedAt(),
                savedComment.getTask().getTaskID(),
                savedComment.getUser().getUserID()
        );
    }

    // Retrieves all comments under a task sorted by comment ID
    @Override
    public List<CommentResponseDTO> getCommentsByTaskId(int taskId) {

        return commentRepository.findAll()
                .stream()
                .filter(comment ->
                        comment.getTask().getTaskID() == taskId)
                .sorted(Comparator.comparingInt(Comment::getCommentID))
                .map(comment -> new CommentResponseDTO(
                        comment.getCommentID(),
                        comment.getText(),
                        comment.getCreatedAt(),
                        comment.getTask().getTaskID(),
                        comment.getUser().getUserID()
                ))
                .collect(Collectors.toList());
    }

    // Deletes a comment using comment ID
    @Override
    public String deleteComment(int commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException(
                                "Comment not found with ID: " + commentId));

        commentRepository.delete(comment);

        return "Comment deleted successfully";
    }
}