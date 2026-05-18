package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;
import com.createtask.createtask.entity.Attachment;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.exception.AttachmentNotFoundException;
import com.createtask.createtask.repository.AttachmentRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.service.impl.AttachmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AttachmentServiceTest {

    private AttachmentRepository attachmentRepository;
    private TaskRepository taskRepository;
    private AttachmentService attachmentService;

    @BeforeEach
    void setUp() {

        attachmentRepository = Mockito.mock(AttachmentRepository.class);
        taskRepository = Mockito.mock(TaskRepository.class);

        attachmentService =
                new AttachmentServiceImpl(
                        attachmentRepository,
                        taskRepository
                );
    }

    // Tests successful attachment creation
    @Test
    void testAddAttachmentSuccess() {

        Task task = new Task();
        task.setTaskID(1);

        Attachment attachment = new Attachment();
        attachment.setAttachmentID(11);
        attachment.setFileName("File.pdf");
        attachment.setFilePath("/docs/File.pdf");
        attachment.setTask(task);

        AttachmentRequestDTO requestDTO =
                new AttachmentRequestDTO(
                        11,
                        "File.pdf",
                        "/docs/File.pdf"
                );

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        when(attachmentRepository.save(any(Attachment.class)))
                .thenReturn(attachment);

        AttachmentResponseDTO response =
                attachmentService.addAttachment(1, requestDTO);

        assertNotNull(response);
        assertEquals(11, response.getAttachmentID());
    }

    // Tests retrieval of attachments by task ID
    @Test
    void testGetAttachmentsByTaskId() {

        Task task = new Task();
        task.setTaskID(1);

        Attachment attachment = new Attachment();
        attachment.setAttachmentID(1);
        attachment.setFileName("Test.pdf");
        attachment.setFilePath("/docs/Test.pdf");
        attachment.setTask(task);

        when(attachmentRepository.findAll())
                .thenReturn(Arrays.asList(attachment));

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(1);

        assertEquals(1, response.size());
    }

    // Tests successful attachment deletion
    @Test
    void testDeleteAttachmentSuccess() {

        Attachment attachment = new Attachment();
        attachment.setAttachmentID(1);

        when(attachmentRepository.findById(1))
                .thenReturn(Optional.of(attachment));

        String response =
                attachmentService.deleteAttachment(1);

        assertEquals(
                "Attachment deleted successfully",
                response
        );
    }

    // Tests attachment deletion failure
    @Test
    void testDeleteAttachmentNotFound() {

        when(attachmentRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                AttachmentNotFoundException.class,
                () -> attachmentService.deleteAttachment(99)
        );
    }

    // Tests empty attachment list
    @Test
    void testGetAttachmentsEmptyList() {

        when(attachmentRepository.findAll())
                .thenReturn(Arrays.asList());

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(1);

        assertTrue(response.isEmpty());
    }

    // Tests attachment filename mapping
    @Test
    void testAttachmentFileNameMapping() {

        Task task = new Task();
        task.setTaskID(1);

        Attachment attachment = new Attachment();
        attachment.setAttachmentID(1);
        attachment.setFileName("Design.pdf");
        attachment.setFilePath("/docs/Design.pdf");
        attachment.setTask(task);

        when(attachmentRepository.findAll())
                .thenReturn(List.of(attachment));

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(1);

        assertEquals(
                "Design.pdf",
                response.get(0).getFileName()
        );
    }

    // Tests attachment file path mapping
    @Test
    void testAttachmentFilePathMapping() {

        Task task = new Task();
        task.setTaskID(1);

        Attachment attachment = new Attachment();
        attachment.setAttachmentID(1);
        attachment.setFilePath("/docs/test.pdf");
        attachment.setTask(task);

        when(attachmentRepository.findAll())
                .thenReturn(List.of(attachment));

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(1);

        assertEquals(
                "/docs/test.pdf",
                response.get(0).getFilePath()
        );
    }

    // Tests attachment sorting by ID
    @Test
    void testAttachmentSortingById() {

        Task task = new Task();
        task.setTaskID(1);

        Attachment a1 = new Attachment();
        a1.setAttachmentID(2);
        a1.setTask(task);

        Attachment a2 = new Attachment();
        a2.setAttachmentID(1);
        a2.setTask(task);

        when(attachmentRepository.findAll())
                .thenReturn(Arrays.asList(a1, a2));

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(1);

        assertEquals(1, response.get(0).getAttachmentID());
    }
}