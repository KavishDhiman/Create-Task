package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Attachment")
public class Attachment {

    @Id
    @Column(name = "AttachmentID")
    private int attachmentID;

    @Column(name = "FileName", nullable = false, length = 255)
    private String fileName;

    @Column(name = "FilePath", nullable = false, length = 255)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;
}
