package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;
import com.createtask.createtask.service.AttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
     *
     * @param taskId     ID of the task to which the attachment belongs
     * @param requestDTO request payload containing attachment details
     * @return saved attachment response details
     */
    @PostMapping("/tasks/{taskId}/attachments")
    public AttachmentResponseDTO addAttachment(
            @PathVariable int taskId,
            @Valid @RequestBody AttachmentRequestDTO requestDTO) {

        return attachmentService.addAttachment(taskId, requestDTO);
    }

    /**
     * Retrieves all attachments associated with a specific task.
     *
     * @param taskId ID of the task
     * @return list of attachment response objects
     */
    @GetMapping("/tasks/{taskId}/attachments")
    public List<AttachmentResponseDTO> getAttachmentsByTaskId(
            @PathVariable int taskId) {

        return attachmentService.getAttachmentsByTaskId(taskId);
    }

    /**
     * Deletes an attachment using its unique attachment ID.
     *
     * @param attachmentId ID of the attachment to delete
     * @return success message after deletion
     */
    @DeleteMapping("/attachments/{attachmentId}")
    public String deleteAttachment(@PathVariable int attachmentId) {

        return attachmentService.deleteAttachment(attachmentId);
    }
}