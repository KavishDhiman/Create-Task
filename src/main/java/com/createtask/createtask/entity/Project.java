package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Project")
public class Project {

    @Id
    @Column(name = "ProjectID")
    private int projectID;

    @Column(name = "ProjectName", nullable = false, length = 255)
    private String projectName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}
