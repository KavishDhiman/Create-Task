package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO used for receiving attachment creation request data.
 */
public class AttachmentRequestDTO {

    /**
     * Unique identifier for the attachment.
     */
    private int attachmentID;

    /**
     * Name of the uploaded file.
     */
    @NotBlank(message = "File name is required")
    private String fileName;

    /**
     * Path where the file is stored.
     */
    @NotBlank(message = "File path is required")
    private String filePath;

    /**
     * Default constructor.
     */
    public AttachmentRequestDTO() {
    }

    /**
     * Parameterized constructor for initializing attachment request data.
     *
     * @param attachmentID unique attachment ID
     * @param fileName     file name
     * @param filePath     file storage path
     */
    public AttachmentRequestDTO(int attachmentID, String fileName, String filePath) {
        this.attachmentID = attachmentID;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    /**
     * Retrieves the attachment ID.
     *
     * @return attachment ID
     */
    public int getAttachmentID() {
        return attachmentID;
    }

    /**
     * Sets the attachment ID.
     *
     * @param attachmentID attachment ID to set
     */
    public void setAttachmentID(int attachmentID) {
        this.attachmentID = attachmentID;
    }

    /**
     * Retrieves the file name.
     *
     * @return file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName file name to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Retrieves the file path.
     *
     * @return file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath file path to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}