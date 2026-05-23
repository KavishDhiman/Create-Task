package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "Attachment")
public class Attachment implements Comparable<Attachment> {

    @Id
    @Column(name = "AttachmentID")
    private int attachmentID;

    @NotBlank(message = "File name is required")
    @Column(name = "FileName", nullable = false, length = 255)
    private String fileName;

    @NotBlank(message = "File path is required")
    @Column(name = "FilePath", nullable = false, length = 255)
    private String filePath;

    @NotNull(message = "Task ID is required")
    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;

    public Attachment() {
    }

    public Attachment(int attachmentID, String fileName, String filePath, Task task) {
        this.attachmentID = attachmentID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment)) return false;
        Attachment that = (Attachment) o;
        return attachmentID == that.attachmentID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachmentID);
    }

    @Override
    public int compareTo(Attachment other) {
        return this.fileName.compareToIgnoreCase(other.fileName);
    }
}