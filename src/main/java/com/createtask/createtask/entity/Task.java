package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Task")
public class Task {

    @Id
    @Column(name = "TaskID")
    private int taskID;

    @Column(name = "TaskName", nullable = false, length = 255)
    private String taskName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "DueDate")
    private LocalDate dueDate;

    @Column(name = "Priority", length = 20)
    private String priority;

    @Column(name = "Status", length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "ProjectID")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}

