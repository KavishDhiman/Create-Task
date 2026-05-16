package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;

import java.util.List;

public interface AttachmentService {

    // Uploads a new attachment for a task
    AttachmentResponseDTO addAttachment(int taskId,
                                        AttachmentRequestDTO requestDTO);

    // Retrieves all attachments under a task sorted by ID
    List<AttachmentResponseDTO> getAttachmentsByTaskId(int taskId);

    // Deletes an attachment using attachment ID
    void deleteAttachment(int attachmentId);
}