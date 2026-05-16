package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByText(String text);

    List<Comment> findByCreatedAt(LocalDateTime createdAt);

    boolean existsByText(String text);
}

