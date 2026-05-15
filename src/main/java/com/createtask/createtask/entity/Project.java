package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Project")
public class Project {

    @Id
    @Column(name = "ProjectID")
    private int projectID;

    @NotBlank(message = "Project name must not be blank")
    @Column(name = "ProjectName", nullable = false, length = 255)
    private String projectName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Start date must not be null")
    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @NotNull(message = "User must not be null")
    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}