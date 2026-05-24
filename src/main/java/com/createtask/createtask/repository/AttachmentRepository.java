package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing database operations on Attachment entities.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

    /**
     * Retrieves all attachments matching the given file name.
     *
     * @param fileName name of the file
     * @return list of matching attachments
     */
    List<Attachment> findByFileName(String fileName);

    /**
     * Retrieves all attachments matching the given file path.
     *
     * @param filePath storage path of the file
     * @return list of matching attachments
     */
    List<Attachment> findByFilePath(String filePath);

    /**
     * Checks whether an attachment exists with the given file name.
     *
     * @param fileName name of the file
     * @return true if attachment exists, otherwise false
     */
    boolean existsByFileName(String fileName);
}