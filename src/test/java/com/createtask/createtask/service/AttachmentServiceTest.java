package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;
import com.createtask.createtask.entity.Attachment;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.exception.AttachmentNotFoundException;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.repository.AttachmentRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.service.impl.AttachmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AttachmentServiceImpl}.
 *
 * <p>Validates all business logic in the attachment service layer using
 * Mockito-based mocks for repository dependencies. Covers both positive
 * (happy path) and negative (exception/edge case) scenarios.
 */
@Tag("unit")
@DisplayName("Attachment Service Tests")
public class AttachmentServiceTest {

    private AttachmentRepository attachmentRepository;
    private TaskRepository taskRepository;
    private AttachmentService attachmentService;

    /**
     * Initialises fresh mock instances and injects them into the service
     * implementation before each test to ensure full test isolation.
     */
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

    /**
     * Verifies that a valid attachment is saved and the response DTO
     * contains the expected attachment ID.
     */
    // Tests successful attachment creation
    @Test
    @DisplayName("Should save attachment and return response DTO with correct ID")
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

        when(attachmentRepository.existsById(11))
                .thenReturn(false);

        when(taskRepository.existsById(1))
                .thenReturn(true);

        when(taskRepository.findById(1))
                .thenReturn(Optional.of(task));

        when(attachmentRepository.save(any(Attachment.class)))
                .thenReturn(attachment);

        AttachmentResponseDTO response =
                attachmentService.addAttachment(1, requestDTO);

        assertNotNull(response);
        assertEquals(11, response.getAttachmentID());
    }

    /**
     * Verifies that all attachments belonging to a given task ID
     * are returned as a list of response DTOs.
     */
    // Tests retrieval of attachments by task ID
    @Test
    @DisplayName("Should return list of attachments for a given task ID")
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

    /**
     * Verifies that an existing attachment is deleted and a success
     * confirmation message is returned.
     */
    // Tests successful attachment deletion
    @Test
    @DisplayName("Should delete attachment and return success message")
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

    /**
     * Verifies that {@link AttachmentNotFoundException} is thrown
     * when attempting to delete an attachment with a non-existent ID.
     */
    // Tests attachment deletion failure when ID does not exist
    @Test
    @DisplayName("Should throw AttachmentNotFoundException when deleting non-existent attachment")
    void testDeleteAttachmentNotFound() {

        when(attachmentRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                AttachmentNotFoundException.class,
                () -> attachmentService.deleteAttachment(99)
        );
    }

    /**
     * Verifies that an empty list is returned when no attachments
     * exist for the given task ID.
     */
    // Tests empty attachment list returned when no attachments exist
    @Test
    @DisplayName("Should return empty list when no attachments exist for task")
    void testGetAttachmentsEmptyList() {

        when(attachmentRepository.findAll())
                .thenReturn(Arrays.asList());

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(1);

        assertTrue(response.isEmpty());
    }

    /**
     * Verifies that the file name from the attachment entity is correctly
     * mapped into the response DTO.
     */
    // Tests attachment filename is correctly mapped to response DTO
    @Test
    @DisplayName("Should map attachment file name correctly into response DTO")
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

    /**
     * Verifies that the file path from the attachment entity is correctly
     * mapped into the response DTO.
     */
    // Tests attachment file path is correctly mapped to response DTO
    @Test
    @DisplayName("Should map attachment file path correctly into response DTO")
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

    /**
     * Verifies that attachments are returned in ascending order
     * of their attachment ID.
     */
    // Tests attachments are returned sorted by attachment ID ascending
    @Test
    @DisplayName("Should return attachments sorted by attachment ID in ascending order")
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

    /**
     * Verifies that the task ID from the attachment entity is correctly
     * mapped into the response DTO.
     */
    // Tests attachment task ID is correctly mapped to response DTO
    @Test
    @DisplayName("Should map task ID correctly into attachment response DTO")
    void testAttachmentTaskIdMapping() {

        Task task = new Task();
        task.setTaskID(10);

        Attachment attachment = new Attachment();
        attachment.setAttachmentID(1);
        attachment.setTask(task);

        when(attachmentRepository.findAll())
                .thenReturn(List.of(attachment));

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(10);

        assertEquals(
                10,
                response.get(0).getTaskID()
        );
    }

    /**
     * Verifies that only attachments belonging to the specified task ID
     * are included in the returned list, filtering out all others.
     */
    // Tests attachments are filtered correctly by task ID
    @Test
    @DisplayName("Should return only attachments belonging to the specified task ID")
    void testAttachmentFilteringByTaskId() {

        Task task1 = new Task();
        task1.setTaskID(1);

        Task task2 = new Task();
        task2.setTaskID(2);

        Attachment a1 = new Attachment();
        a1.setAttachmentID(1);
        a1.setTask(task1);

        Attachment a2 = new Attachment();
        a2.setAttachmentID(2);
        a2.setTask(task2);

        when(attachmentRepository.findAll())
                .thenReturn(Arrays.asList(a1, a2));

        List<AttachmentResponseDTO> response =
                attachmentService.getAttachmentsByTaskId(1);

        assertEquals(1, response.size());
    }

    /**
     * Verifies that {@link DuplicateResourceException} is thrown when
     * an attachment with an already-existing ID is submitted, and that
     * the repository save operation is never invoked.
     */
    // Tests exception is thrown when duplicate attachment ID is used
    @Test
    @DisplayName("Should throw DuplicateResourceException when attachment ID already exists")
    void testAddAttachmentDuplicateId() {

        AttachmentRequestDTO requestDTO =
                new AttachmentRequestDTO(
                        11,
                        "File.pdf",
                        "/docs/File.pdf"
                );

        when(attachmentRepository.existsById(11))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> attachmentService.addAttachment(1, requestDTO)
        );

        verify(attachmentRepository, never())
                .save(any());
    }

    /**
     * Verifies that a {@link RuntimeException} is thrown when the provided
     * task ID does not exist, and that no partial data is persisted.
     */
    // Tests exception is thrown when task ID does not exist during attachment creation
    @Test
    @DisplayName("Should throw RuntimeException when task ID is not found")
    void testAddAttachmentTaskNotFound() {

        AttachmentRequestDTO requestDTO =
                new AttachmentRequestDTO(
                        11,
                        "File.pdf",
                        "/docs/File.pdf"
                );

        when(attachmentRepository.existsById(11))
                .thenReturn(false);

        when(taskRepository.existsById(99))
                .thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> attachmentService.addAttachment(99, requestDTO)
        );

        verify(attachmentRepository, never())
                .save(any());
    }

    /**
     * Verifies that a {@link RuntimeException} is thrown when the file path
     * does not begin with a forward slash, and that no data is persisted.
     */
    // Tests exception is thrown when file path format is invalid
    @Test
    @DisplayName("Should throw RuntimeException when file path format is invalid")
    void testAddAttachmentInvalidFilePath() {

        AttachmentRequestDTO requestDTO =
                new AttachmentRequestDTO(
                        11,
                        "File.pdf",
                        "docs/File.pdf"    // missing leading slash
                );

        when(attachmentRepository.existsById(11))
                .thenReturn(false);

        when(taskRepository.existsById(1))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> attachmentService.addAttachment(1, requestDTO)
        );

        verify(attachmentRepository, never())
                .save(any());
    }
}