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

/**
 * Service implementation class responsible for comment-related business logic.
 */
@Service
public class CommentServiceImpl implements CommentService {

    /**
     * Repository dependency for comment database operations.
     */
    private final CommentRepository commentRepository;

    /**
     * Repository dependency for task database operations.
     */
    private final TaskRepository taskRepository;

    /**
     * Repository dependency for user database operations.
     */
    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection for repositories.
     *
     * @param commentRepository repository for comment operations
     * @param taskRepository    repository for task operations
     * @param userRepository    repository for user operations
     */
    public CommentServiceImpl(CommentRepository commentRepository,
                              TaskRepository taskRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds a new comment for a specific task.
     *
     * @param taskId     ID of the task to which the comment belongs
     * @param requestDTO request payload containing comment details
     * @return saved comment response object
     */
    @Override
    public CommentResponseDTO addComment(int taskId,
                                         CommentRequestDTO requestDTO) {

        // Checks whether the comment ID already exists
        if (commentRepository.existsById(requestDTO.getCommentID())) {
            throw new DuplicateResourceException(
                    "Comment already exists with ID: " + requestDTO.getCommentID());
        }

        // Validates whether the provided task ID exists
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task ID not found: " + taskId);
        }

        // Validates whether the provided user ID exists
        if (!userRepository.existsById(requestDTO.getUserID())) {
            throw new RuntimeException("User ID not found: " + requestDTO.getUserID());
        }

        // Retrieves task entity from database
        Task task = taskRepository.findById(taskId).orElseThrow();

        // Retrieves user entity from database
        AppUser user = userRepository.findById(requestDTO.getUserID()).orElseThrow();

        // Creates a new comment entity
        Comment comment = new Comment();

        // Sets comment properties from request DTO
        comment.setCommentID(requestDTO.getCommentID());
        comment.setText(requestDTO.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setTask(task);
        comment.setUser(user);

        // Saves comment entity into database
        Comment savedComment = commentRepository.save(comment);

        // Converts entity into response DTO
        return new CommentResponseDTO(
                savedComment.getCommentID(),
                savedComment.getText(),
                savedComment.getCreatedAt(),
                savedComment.getTask().getTaskID(),
                savedComment.getUser().getUserID()
        );
    }

    /**
     * Retrieves all comments associated with a specific task.
     *
     * @param taskId ID of the task
     * @return list of comment response objects
     */
    @Override
    public List<CommentResponseDTO> getCommentsByTaskId(int taskId) {

        return commentRepository.findAll()
                .stream()

                // Filters comments belonging to the given task ID
                .filter(comment ->
                        comment.getTask().getTaskID() == taskId)

                // Sorts comments by comment ID
                .sorted(Comparator.comparingInt(Comment::getCommentID))

                // Converts comment entities into response DTOs
                .map(comment -> new CommentResponseDTO(
                        comment.getCommentID(),
                        comment.getText(),
                        comment.getCreatedAt(),
                        comment.getTask().getTaskID(),
                        comment.getUser().getUserID()
                ))

                // Collects results into a list
                .collect(Collectors.toList());
    }

    /**
     * Deletes a comment using its unique comment ID.
     *
     * @param commentId ID of the comment to delete
     * @return success message after deletion
     */
    @Override
    public String deleteComment(int commentId) {

        // Retrieves comment entity or throws exception if not found
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException(
                                "Comment not found with ID: " + commentId));

        // Deletes comment entity from database
        commentRepository.delete(comment);

        // Returns deletion success message
        return "Comment deleted successfully";
    }
}