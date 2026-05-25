package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;
import com.createtask.createtask.service.AttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for managing attachment-related APIs.
 * Provides endpoints for adding, retrieving, and deleting attachments.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Attachment Management", description = "APIs for managing attachments")
public class AttachmentController {

    /**
     * Service layer dependency for attachment operations.
     */
    private final AttachmentService attachmentService;

    /**
     * Constructor-based dependency injection for AttachmentService.
     *
     * @param attachmentService service used for attachment operations
     */
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * Uploads a new attachment for a specific task.
     * Returns HTTP 201 Created on successful creation.
     *
     * @param taskId     ID of the task to which the attachment belongs
     * @param requestDTO request payload containing attachment details
     * @return 201 Created with the saved attachment response details
     */
    @PostMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<AttachmentResponseDTO> addAttachment(
            @PathVariable int taskId,
            @Valid @RequestBody AttachmentRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attachmentService.addAttachment(taskId, requestDTO));
    }

    /**
     * Retrieves all attachments associated with a specific task.
     * Returns HTTP 200 OK with the list of attachments.
     *
     * @param taskId ID of the task
     * @return 200 OK with list of attachment response objects
     */
    @GetMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByTaskId(
            @PathVariable int taskId) {

        return ResponseEntity.ok(attachmentService.getAttachmentsByTaskId(taskId));
    }

    /**
     * Deletes an attachment using its unique attachment ID.
     * Returns HTTP 200 OK with a confirmation message on success.
     *
     * @param attachmentId ID of the attachment to delete
     * @return 200 OK with success message after deletion
     */
    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<String> deleteAttachment(@PathVariable int attachmentId) {

        return ResponseEntity.ok(attachmentService.deleteAttachment(attachmentId));
    }
}