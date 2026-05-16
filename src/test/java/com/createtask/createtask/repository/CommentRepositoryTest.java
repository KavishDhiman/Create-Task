package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testFindAll() {

        List<Comment> comments = commentRepository.findAll();

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
    }

    @Test
    void testFindByIdExists() {

        Optional<Comment> comment = commentRepository.findById(1);

        assertTrue(comment.isPresent());
    }

    @Test
    void testFindByIdNotExists() {

        Optional<Comment> comment = commentRepository.findById(9999);

        assertFalse(comment.isPresent());
    }

    @Test
    void testCommentTextNotNull() {

        Comment comment = commentRepository.findById(1).orElse(null);

        assertNotNull(comment);
        assertNotNull(comment.getText());
    }

    @Test
    void testCommentCreatedAtNotNull() {

        Comment comment = commentRepository.findById(1).orElse(null);

        assertNotNull(comment);
        assertNotNull(comment.getCreatedAt());
    }

    @Test
    void testCommentTaskNotNull() {

        Comment comment = commentRepository.findById(1).orElse(null);

        assertNotNull(comment);
        assertNotNull(comment.getTask());
    }

    @Test
    void testCommentUserNotNull() {

        Comment comment = commentRepository.findById(1).orElse(null);

        assertNotNull(comment);
        assertNotNull(comment.getUser());
    }

    @Test
    void testCommentCountGreaterThanZero() {

        long count = commentRepository.count();

        assertTrue(count > 0);
    }
}