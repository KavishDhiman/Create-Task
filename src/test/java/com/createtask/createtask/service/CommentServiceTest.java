package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.entity.Comment;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.exception.CommentNotFoundException;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.repository.CommentRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CommentServiceImpl}.
 *
 * <p>Validates all business logic in the comment service layer using
 * Mockito-based mocks for repository dependencies. Covers both positive
 * (happy path) and negative (exception/edge case) scenarios.
 */
@Tag("unit")
@DisplayName("Comment Service Tests")
public class CommentServiceTest {

    private CommentRepository commentRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private CommentService commentService;

    /**
     * Initialises fresh mock instances and injects them into the service
     * implementation before each test to ensure full test isolation.
     */
    @BeforeEach
    void setUp() {

        commentRepository = Mockito.mock(CommentRepository.class);

        taskRepository = Mockito.mock(TaskRepository.class);

        userRepository = Mockito.mock(UserRepository.class);

        commentService =
                new CommentServiceImpl(
                        commentRepository,
                        taskRepository,
                        userRepository
                );
    }

    /**
     * Verifies that a valid comment is saved and the response DTO
     * contains the expected comment ID.
     */
    // Tests successful comment creation
    @Test
    @DisplayName("Should save comment and return response DTO with correct ID")
    void testAddCommentSuccess() {

        Task task = new Task();
        task.setTaskID(1);

        AppUser user = new AppUser();
        user.setUserID(1);

        Comment comment = new Comment();
        comment.setCommentID(11);
        comment.setText("Testing");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setTask(task);
        comment.setUser(user);

        CommentRequestDTO requestDTO =
                new CommentRequestDTO(
                        11,
                        "Testing",
                        1
                );

        when(commentRepository.existsById(11))
                .thenReturn(false);

        when(taskRepository.existsById(1))
                .thenReturn(true);

        when(userRepository.existsById(1))
                .thenReturn(true);

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        CommentResponseDTO response =
                commentService.addComment(1, requestDTO);

        assertNotNull(response);
        assertEquals(11, response.getCommentID());
    }

    /**
     * Verifies that all comments belonging to a given task ID
     * are returned as a list of response DTOs.
     */
    // Tests retrieval of comments by task ID
    @Test
    @DisplayName("Should return list of comments for a given task ID")
    void testGetCommentsByTaskId() {

        Task task = new Task();
        task.setTaskID(1);

        AppUser user = new AppUser();
        user.setUserID(1);

        Comment comment = new Comment();
        comment.setCommentID(1);
        comment.setText("Test");
        comment.setTask(task);
        comment.setUser(user);

        when(commentRepository.findAll())
                .thenReturn(Arrays.asList(comment));

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(1);

        assertEquals(1, response.size());
    }

    /**
     * Verifies that an existing comment is deleted and a success
     * confirmation message is returned.
     */
    // Tests successful comment deletion
    @Test
    @DisplayName("Should delete comment and return success message")
    void testDeleteCommentSuccess() {

        Comment comment = new Comment();
        comment.setCommentID(1);

        when(commentRepository.findById(1))
                .thenReturn(Optional.of(comment));

        String response =
                commentService.deleteComment(1);

        assertEquals(
                "Comment deleted successfully",
                response
        );
    }

    /**
     * Verifies that {@link CommentNotFoundException} is thrown
     * when attempting to delete a comment with a non-existent ID.
     */
    // Tests comment deletion failure when ID does not exist
    @Test
    @DisplayName("Should throw CommentNotFoundException when deleting non-existent comment")
    void testDeleteCommentNotFound() {

        when(commentRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.deleteComment(99)
        );
    }

    /**
     * Verifies that an empty list is returned when no comments
     * exist for the given task ID.
     */
    // Tests empty comment list returned when no comments exist
    @Test
    @DisplayName("Should return empty list when no comments exist for task")
    void testGetCommentsEmptyList() {

        when(commentRepository.findAll())
                .thenReturn(Arrays.asList());

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(1);

        assertTrue(response.isEmpty());
    }

    /**
     * Verifies that the comment text from the entity is correctly
     * mapped into the response DTO.
     */
    // Tests comment text is correctly mapped to response DTO
    @Test
    @DisplayName("Should map comment text correctly into response DTO")
    void testCommentTextMapping() {

        Task task = new Task();
        task.setTaskID(1);

        AppUser user = new AppUser();
        user.setUserID(1);

        Comment comment = new Comment();
        comment.setCommentID(1);
        comment.setText("Hello");
        comment.setTask(task);
        comment.setUser(user);

        when(commentRepository.findAll())
                .thenReturn(List.of(comment));

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(1);

        assertEquals(
                "Hello",
                response.get(0).getText()
        );
    }

    /**
     * Verifies that the user ID from the comment entity is correctly
     * mapped into the response DTO.
     */
    // Tests comment user ID is correctly mapped to response DTO
    @Test
    @DisplayName("Should map user ID correctly into comment response DTO")
    void testCommentUserMapping() {

        Task task = new Task();
        task.setTaskID(1);

        AppUser user = new AppUser();
        user.setUserID(5);

        Comment comment = new Comment();
        comment.setCommentID(1);
        comment.setTask(task);
        comment.setUser(user);

        when(commentRepository.findAll())
                .thenReturn(List.of(comment));

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(1);

        assertEquals(
                5,
                response.get(0).getUserID()
        );
    }

    /**
     * Verifies that comments are returned in ascending order
     * of their comment ID.
     */
    // Tests comments are returned sorted by comment ID ascending
    @Test
    @DisplayName("Should return comments sorted by comment ID in ascending order")
    void testCommentSortingById() {

        Task task = new Task();
        task.setTaskID(1);

        AppUser user = new AppUser();
        user.setUserID(1);

        Comment c1 = new Comment();
        c1.setCommentID(2);
        c1.setTask(task);
        c1.setUser(user);

        Comment c2 = new Comment();
        c2.setCommentID(1);
        c2.setTask(task);
        c2.setUser(user);

        when(commentRepository.findAll())
                .thenReturn(Arrays.asList(c1, c2));

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(1);

        assertEquals(1, response.get(0).getCommentID());
    }

    /**
     * Verifies that the task ID from the comment entity is correctly
     * mapped into the response DTO.
     */
    // Tests comment task ID is correctly mapped to response DTO
    @Test
    @DisplayName("Should map task ID correctly into comment response DTO")
    void testCommentTaskIdMapping() {

        Task task = new Task();
        task.setTaskID(10);

        AppUser user = new AppUser();
        user.setUserID(1);

        Comment comment = new Comment();
        comment.setCommentID(1);
        comment.setTask(task);
        comment.setUser(user);

        when(commentRepository.findAll())
                .thenReturn(List.of(comment));

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(10);

        assertEquals(
                10,
                response.get(0).getTaskID()
        );
    }

    /**
     * Verifies that only comments belonging to the specified task ID
     * are included in the returned list, filtering out all others.
     */
    // Tests comments are filtered correctly by task ID
    @Test
    @DisplayName("Should return only comments belonging to the specified task ID")
    void testCommentFilteringByTaskId() {

        Task task1 = new Task();
        task1.setTaskID(1);

        Task task2 = new Task();
        task2.setTaskID(2);

        AppUser user = new AppUser();
        user.setUserID(1);

        Comment c1 = new Comment();
        c1.setCommentID(1);
        c1.setTask(task1);
        c1.setUser(user);

        Comment c2 = new Comment();
        c2.setCommentID(2);
        c2.setTask(task2);
        c2.setUser(user);

        when(commentRepository.findAll())
                .thenReturn(Arrays.asList(c1, c2));

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(1);

        assertEquals(1, response.size());
    }

    /**
     * Verifies that {@link DuplicateResourceException} is thrown when
     * a comment with an already-existing ID is submitted, and that
     * the repository save operation is never invoked.
     */
    // Tests exception is thrown when duplicate comment ID is used
    @Test
    @DisplayName("Should throw DuplicateResourceException when comment ID already exists")
    void testAddCommentDuplicateId() {

        CommentRequestDTO requestDTO =
                new CommentRequestDTO(
                        11,
                        "Testing",
                        1
                );

        when(commentRepository.existsById(11))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> commentService.addComment(1, requestDTO)
        );

        verify(commentRepository, never())
                .save(any());
    }

    /**
     * Verifies that a {@link RuntimeException} is thrown when the provided
     * task ID does not exist, and that no partial data is persisted.
     */
    // Tests exception is thrown when task ID does not exist during comment creation
    @Test
    @DisplayName("Should throw RuntimeException when task ID is not found")
    void testAddCommentTaskNotFound() {

        CommentRequestDTO requestDTO =
                new CommentRequestDTO(
                        11,
                        "Testing",
                        1
                );

        when(commentRepository.existsById(11))
                .thenReturn(false);

        when(taskRepository.existsById(99))
                .thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> commentService.addComment(99, requestDTO)
        );

        verify(commentRepository, never())
                .save(any());
    }

    /**
     * Verifies that a {@link RuntimeException} is thrown when the provided
     * user ID does not exist, and that no partial data is persisted.
     */
    // Tests exception is thrown when user ID does not exist during comment creation
    @Test
    @DisplayName("Should throw RuntimeException when user ID is not found")
    void testAddCommentUserNotFound() {

        CommentRequestDTO requestDTO =
                new CommentRequestDTO(
                        11,
                        "Testing",
                        99
                );

        when(commentRepository.existsById(11))
                .thenReturn(false);

        when(taskRepository.existsById(1))
                .thenReturn(true);

        when(userRepository.existsById(99))
                .thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> commentService.addComment(1, requestDTO)
        );

        verify(commentRepository, never())
                .save(any());
    }
}