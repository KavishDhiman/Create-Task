package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

    List<Attachment> findByFileName(String fileName);

    List<Attachment> findByFilePath(String filePath);

    boolean existsByFileName(String fileName);
}

