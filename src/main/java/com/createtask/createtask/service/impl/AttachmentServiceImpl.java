package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;
import com.createtask.createtask.entity.Attachment;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.exception.AttachmentNotFoundException;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.exception.TaskNotFoundException;
import com.createtask.createtask.repository.AttachmentRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.service.AttachmentService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation class responsible for attachment-related business logic.
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    /**
     * Repository dependency for attachment database operations.
     */
    private final AttachmentRepository attachmentRepository;

    /**
     * Repository dependency for task database operations.
     */
    private final TaskRepository taskRepository;

    /**
     * Constructor-based dependency injection for repositories.
     *
     * @param attachmentRepository repository for attachment operations
     * @param taskRepository       repository for task operations
     */
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository,
                                 TaskRepository taskRepository) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Uploads a new attachment for a specific task.
     *
     * @param taskId     ID of the task to which the attachment belongs
     * @param requestDTO request payload containing attachment details
     * @return saved attachment response object
     */
    @Override
    public AttachmentResponseDTO addAttachment(int taskId,
                                               AttachmentRequestDTO requestDTO) {

        // Checks whether the attachment ID already exists
        if (attachmentRepository.existsById(requestDTO.getAttachmentID())) {
            throw new DuplicateResourceException(
                    "Attachment already exists with ID: " + requestDTO.getAttachmentID());
        }

        // Validates whether the provided task ID exists
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task ID not found: " + taskId);
        }

        // Validates file path format
        String filePath = requestDTO.getFilePath();
        if (filePath == null || !filePath.matches("^(/[\\w\\-\\.]+)+$")) {
            throw new IllegalArgumentException("Please enter valid file path (e.g. /uploads/report.pdf or /path/to/file)");
        }

        // Retrieves task entity from database
        Task task = taskRepository.findById(taskId).orElseThrow();

        // Creates a new attachment entity
        Attachment attachment = new Attachment();

        // Sets attachment properties from request DTO
        attachment.setAttachmentID(requestDTO.getAttachmentID());
        attachment.setFileName(requestDTO.getFileName());
        attachment.setFilePath(filePath);
        attachment.setTask(task);

        // Saves attachment entity into database
        Attachment savedAttachment = attachmentRepository.save(attachment);

        // Converts entity into response DTO
        return new AttachmentResponseDTO(
                savedAttachment.getAttachmentID(),
                savedAttachment.getFileName(),
                savedAttachment.getFilePath(),
                savedAttachment.getTask().getTaskID()
        );
    }

    /**
     * Retrieves all attachments associated with a specific task.
     *
     * @param taskId ID of the task
     * @return list of attachment response objects
     */
    @Override
    public List<AttachmentResponseDTO> getAttachmentsByTaskId(int taskId) {

        return attachmentRepository.findAll()
                .stream()

                // Filters attachments belonging to the given task ID
                .filter(attachment ->
                        attachment.getTask().getTaskID() == taskId)

                // Sorts attachments by attachment ID
                .sorted(Comparator.comparingInt(Attachment::getAttachmentID))

                // Converts attachment entities into response DTOs
                .map(attachment -> new AttachmentResponseDTO(
                        attachment.getAttachmentID(),
                        attachment.getFileName(),
                        attachment.getFilePath(),
                        attachment.getTask().getTaskID()
                ))

                // Collects results into a list
                .collect(Collectors.toList());
    }

    /**
     * Deletes an attachment using its unique attachment ID.
     *
     * @param attachmentId ID of the attachment to delete
     * @return success message after deletion
     */
    @Override
    public String deleteAttachment(int attachmentId) {

        // Retrieves attachment entity or throws exception if not found
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() ->
                        new AttachmentNotFoundException(
                                "Attachment not found with ID: " + attachmentId));

        // Deletes attachment entity from database
        attachmentRepository.delete(attachment);

        // Returns deletion success message
        return "Attachment deleted successfully";
    }
}