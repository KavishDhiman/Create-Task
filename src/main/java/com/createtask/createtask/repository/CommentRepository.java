package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing database operations on Comment entities.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}