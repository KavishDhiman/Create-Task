package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.CommentRequestDTO;
import com.createtask.createtask.dto.response.CommentResponseDTO;
import com.createtask.createtask.entity.Comment;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.User;
import com.createtask.createtask.exception.CommentNotFoundException;
import com.createtask.createtask.repository.CommentRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    private CommentRepository commentRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private CommentService commentService;

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

    // Tests successful comment creation
    @Test
    void testAddCommentSuccess() {

        Task task = new Task();
        task.setTaskID(1);

        User user = new User();
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

    // Tests retrieval of comments by task ID
    @Test
    void testGetCommentsByTaskId() {

        Task task = new Task();
        task.setTaskID(1);

        User user = new User();
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

    // Tests successful comment deletion
    @Test
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

    // Tests comment deletion failure
    @Test
    void testDeleteCommentNotFound() {

        when(commentRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.deleteComment(99)
        );
    }

    // Tests empty comment list
    @Test
    void testGetCommentsEmptyList() {

        when(commentRepository.findAll())
                .thenReturn(Arrays.asList());

        List<CommentResponseDTO> response =
                commentService.getCommentsByTaskId(1);

        assertTrue(response.isEmpty());
    }

    // Tests comment text mapping
    @Test
    void testCommentTextMapping() {

        Task task = new Task();
        task.setTaskID(1);

        User user = new User();
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

    // Tests comment user mapping
    @Test
    void testCommentUserMapping() {

        Task task = new Task();
        task.setTaskID(1);

        User user = new User();
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

    // Tests comment sorting by ID
    @Test
    void testCommentSortingById() {

        Task task = new Task();
        task.setTaskID(1);

        User user = new User();
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
}