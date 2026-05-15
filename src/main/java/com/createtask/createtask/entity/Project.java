package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "Project")
public class Project {

    @Id
    @Column(name = "ProjectID")
    @NotNull(message = "Project ID must not be null. Please provide a valid project identifier.")
    private Integer projectID;

    @NotBlank(message = "Project name must not be blank. Please provide a meaningful name for the project.")
    @Size(max = 255, message = "Project name must not exceed 255 characters. Please shorten the project name.")
    @Column(name = "ProjectName", nullable = false, length = 255)
    private String projectName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Start date must not be null. Please provide a valid start date for the project.")
    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @NotNull(message = "User must not be null. Every project must be assigned to a valid registered user.")
    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    public Project() {}

    public Project(Integer projectID, String projectName, String description,
                   LocalDate startDate, LocalDate endDate, User user) {
        this.projectID   = projectID;
        this.projectName = projectName;
        this.description = description;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.user        = user;
    }

    public Integer getProjectID()      { return projectID; }
    public String getProjectName()     { return projectName; }
    public String getDescription()     { return description; }
    public LocalDate getStartDate()    { return startDate; }
    public LocalDate getEndDate()      { return endDate; }
    public User getUser()              { return user; }

    public void setProjectID(Integer projectID)        { this.projectID = projectID; }
    public void setProjectName(String projectName)     { this.projectName = projectName; }
    public void setDescription(String description)     { this.description = description; }
    public void setStartDate(LocalDate startDate)      { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate)          { this.endDate = endDate; }
    public void setUser(User user)                     { this.user = user; }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", userID=" + (user != null ? user.getUserID() : "null") +
                '}';
    }
}