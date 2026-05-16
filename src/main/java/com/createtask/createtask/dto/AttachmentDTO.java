package com.createtask.createtask.dto;

public class AttachmentDTO {

    private int attachmentID;
    private String fileName;
    private String filePath;
    private int taskID;

    public AttachmentDTO() {
    }

    public AttachmentDTO(int attachmentID, String fileName, String filePath, int taskID) {
        this.attachmentID = attachmentID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.taskID = taskID;
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

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}