package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for performing database operations on Comment entities.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Retrieves all comments matching the given text.
     *
     * @param text comment text
     * @return list of matching comments
     */
    List<Comment> findByText(String text);

    /**
     * Retrieves all comments created at the given timestamp.
     *
     * @param createdAt comment creation timestamp
     * @return list of matching comments
     */
    List<Comment> findByCreatedAt(LocalDateTime createdAt);

    /**
     * Checks whether a comment exists with the given text.
     *
     * @param text comment text
     * @return true if comment exists, otherwise false
     */
    boolean existsByText(String text);
}