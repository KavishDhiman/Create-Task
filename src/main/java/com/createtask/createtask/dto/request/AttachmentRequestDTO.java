package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AttachmentRequestDTO {

    private int attachmentID;

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotBlank(message = "File path is required")
    private String filePath;

    public AttachmentRequestDTO() {
    }

    public AttachmentRequestDTO(int attachmentID, String fileName, String filePath) {
        this.attachmentID = attachmentID;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public int getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(int attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}