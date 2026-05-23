package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;
import com.createtask.createtask.service.AttachmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Attachment Management", description = "APIs for managing attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    // Uploads a new attachment for a task
    @PostMapping("/tasks/{taskId}/attachments")
    public AttachmentResponseDTO addAttachment(
            @PathVariable int taskId,
            @Valid @RequestBody AttachmentRequestDTO requestDTO) {

        return attachmentService.addAttachment(taskId, requestDTO);
    }

    // Retrieves all attachments under a task sorted by attachment ID
    @GetMapping("/tasks/{taskId}/attachments")
    public List<AttachmentResponseDTO> getAttachmentsByTaskId(
            @PathVariable int taskId) {

        return attachmentService.getAttachmentsByTaskId(taskId);
    }

    // Deletes an attachment using attachment ID
    @DeleteMapping("/attachments/{attachmentId}")
    public String deleteAttachment(@PathVariable int attachmentId) {

        return attachmentService.deleteAttachment(attachmentId);
    }
}