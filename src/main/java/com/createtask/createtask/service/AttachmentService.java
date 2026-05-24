package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;

import java.util.List;

/**
 * Service interface defining business operations for attachment management.
 */
public interface AttachmentService {

    /**
     * Uploads a new attachment for a specific task.
     *
     * @param taskId     ID of the task to which the attachment belongs
     * @param requestDTO request payload containing attachment details
     * @return saved attachment response object
     */
    AttachmentResponseDTO addAttachment(int taskId,
                                        AttachmentRequestDTO requestDTO);

    /**
     * Retrieves all attachments associated with a specific task.
     *
     * @param taskId ID of the task
     * @return list of attachment response objects
     */
    List<AttachmentResponseDTO> getAttachmentsByTaskId(int taskId);

    /**
     * Deletes an attachment using its unique attachment ID.
     *
     * @param attachmentId ID of the attachment to delete
     * @return success message after deletion
     */
    String deleteAttachment(int attachmentId);
}