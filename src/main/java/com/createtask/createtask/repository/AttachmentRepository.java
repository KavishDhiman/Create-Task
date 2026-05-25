package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing database operations on Attachment entities.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}