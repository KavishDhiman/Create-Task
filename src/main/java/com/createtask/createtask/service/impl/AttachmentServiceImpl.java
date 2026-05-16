package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.AttachmentRequestDTO;
import com.createtask.createtask.dto.response.AttachmentResponseDTO;
import com.createtask.createtask.entity.Attachment;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.exception.AttachmentNotFoundException;
import com.createtask.createtask.repository.AttachmentRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.service.AttachmentService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository,
                                 TaskRepository taskRepository) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
    }

    // Uploads a new attachment for a task
    @Override
    public AttachmentResponseDTO addAttachment(int taskId,
                                               AttachmentRequestDTO requestDTO) {

        Task task = taskRepository.findById(taskId).orElseThrow();

        Attachment attachment = new Attachment();

        attachment.setAttachmentID(requestDTO.getAttachmentID());
        attachment.setFileName(requestDTO.getFileName());
        attachment.setFilePath(requestDTO.getFilePath());
        attachment.setTask(task);

        Attachment savedAttachment = attachmentRepository.save(attachment);

        return new AttachmentResponseDTO(
                savedAttachment.getAttachmentID(),
                savedAttachment.getFileName(),
                savedAttachment.getFilePath(),
                savedAttachment.getTask().getTaskID()
        );
    }

    // Retrieves all attachments under a task sorted by attachment ID
    @Override
    public List<AttachmentResponseDTO> getAttachmentsByTaskId(int taskId) {

        return attachmentRepository.findAll()
                .stream()
                .filter(attachment ->
                        attachment.getTask().getTaskID() == taskId)
                .sorted(Comparator.comparingInt(Attachment::getAttachmentID))
                .map(attachment -> new AttachmentResponseDTO(
                        attachment.getAttachmentID(),
                        attachment.getFileName(),
                        attachment.getFilePath(),
                        attachment.getTask().getTaskID()
                ))
                .collect(Collectors.toList());
    }

    // Deletes an attachment using attachment ID
    @Override
    public void deleteAttachment(int attachmentId) {

        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() ->
                        new AttachmentNotFoundException(
                                "Attachment not found with ID: "
                                        + attachmentId));

        attachmentRepository.delete(attachment);
    }
}