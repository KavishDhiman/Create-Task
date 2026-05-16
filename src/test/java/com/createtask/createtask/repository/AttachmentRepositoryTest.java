package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Attachment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttachmentRepositoryTest {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    void testFindAll() {

        List<Attachment> attachments = attachmentRepository.findAll();

        assertNotNull(attachments);
        assertFalse(attachments.isEmpty());
    }

    @Test
    void testFindByIdExists() {

        Optional<Attachment> attachment = attachmentRepository.findById(1);

        assertTrue(attachment.isPresent());
    }

    @Test
    void testFindByIdNotExists() {

        Optional<Attachment> attachment = attachmentRepository.findById(9999);

        assertFalse(attachment.isPresent());
    }

    @Test
    void testAttachmentFileNameNotNull() {

        Attachment attachment = attachmentRepository.findById(1).orElse(null);

        assertNotNull(attachment);
        assertNotNull(attachment.getFileName());
    }

    @Test
    void testAttachmentFilePathNotNull() {

        Attachment attachment = attachmentRepository.findById(1).orElse(null);

        assertNotNull(attachment);
        assertNotNull(attachment.getFilePath());
    }

    @Test
    void testAttachmentTaskNotNull() {

        Attachment attachment = attachmentRepository.findById(1).orElse(null);

        assertNotNull(attachment);
        assertNotNull(attachment.getTask());
    }

    @Test
    void testAttachmentIdPositive() {

        Attachment attachment = attachmentRepository.findById(1).orElse(null);

        assertNotNull(attachment);
        assertTrue(attachment.getAttachmentID() > 0);
    }

    @Test
    void testAttachmentCountGreaterThanZero() {

        long count = attachmentRepository.count();

        assertTrue(count > 0);
    }
}