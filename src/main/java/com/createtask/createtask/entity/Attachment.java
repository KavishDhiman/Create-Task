package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Entity representing an attachment associated with a task.
 */
@Entity
@Table(name = "Attachment")
public class Attachment implements Comparable<Attachment> {

    /**
     * Unique identifier for the attachment.
     */
    @Id
    @Column(name = "AttachmentID")
    private int attachmentID;

    /**
     * Name of the uploaded file.
     */
    @NotBlank(message = "File name is required")
    @Column(name = "FileName", nullable = false, length = 255)
    private String fileName;

    /**
     * File storage path of the attachment.
     */
    @NotBlank(message = "File path is required")
    @Column(name = "FilePath", nullable = false, length = 255)
    private String filePath;

    /**
     * Task associated with the attachment.
     */
    @NotNull(message = "Task ID is required")
    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;

    /**
     * Default constructor.
     */
    public Attachment() {
    }

    /**
     * Parameterized constructor for initializing attachment entity fields.
     *
     * @param attachmentID unique attachment ID
     * @param fileName     file name
     * @param filePath     file storage path
     * @param task         associated task entity
     */
    public Attachment(int attachmentID, String fileName, String filePath, Task task) {
        this.attachmentID = attachmentID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.task = task;
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
     * Retrieves the associated task.
     *
     * @return associated task entity
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the associated task.
     *
     * @param task task entity to set
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Compares two attachment objects based on attachment ID.
     *
     * @param o object to compare
     * @return true if both objects have the same attachment ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment)) return false;
        Attachment that = (Attachment) o;
        return attachmentID == that.attachmentID;
    }

    /**
     * Generates hash code using attachment ID.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(attachmentID);
    }

    /**
     * Compares attachments alphabetically by file name.
     *
     * @param other another attachment object
     * @return comparison result based on file name
     */
    @Override
    public int compareTo(Attachment other) {
        return this.fileName.compareToIgnoreCase(other.fileName);
    }
}