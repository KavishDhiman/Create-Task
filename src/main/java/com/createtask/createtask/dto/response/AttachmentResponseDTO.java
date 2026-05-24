package com.createtask.createtask.dto.response;

/**
 * DTO used for sending attachment response data.
 */
public class AttachmentResponseDTO {

    /**
     * Unique identifier for the attachment.
     */
    private int attachmentID;

    /**
     * Name of the uploaded file.
     */
    private String fileName;

    /**
     * Path where the file is stored.
     */
    private String filePath;

    /**
     * ID of the associated task.
     */
    private int taskID;

    /**
     * Default constructor.
     */
    public AttachmentResponseDTO() {
    }

    /**
     * Parameterized constructor for initializing attachment response data.
     *
     * @param attachmentID unique attachment ID
     * @param fileName     file name
     * @param filePath     file storage path
     * @param taskID       associated task ID
     */
    public AttachmentResponseDTO(int attachmentID, String fileName, String filePath, int taskID) {
        this.attachmentID = attachmentID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.taskID = taskID;
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

    /**
     * Retrieves the task ID.
     *
     * @return task ID
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * Sets the task ID.
     *
     * @param taskID task ID to set
     */
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}