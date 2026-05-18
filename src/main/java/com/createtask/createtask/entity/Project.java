package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Project")
public class Project implements Comparable<Project> {

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
    private AppUser user;

    public Project() {}

    public Project(Integer projectID, String projectName, String description,
                   LocalDate startDate, LocalDate endDate, AppUser user) {
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
    public AppUser getUser()              { return user; }

    public void setProjectID(Integer projectID)        { this.projectID = projectID; }
    public void setProjectName(String projectName)     { this.projectName = projectName; }
    public void setDescription(String description)     { this.description = description; }
    public void setStartDate(LocalDate startDate)      { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate)          { this.endDate = endDate; }
    public void setUser(AppUser user)                     { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectID, project.projectID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectID);
    }


    @Override
    public int compareTo(Project other) {
        if (this.startDate == null && other.startDate == null) return 0;
        if (this.startDate == null) return 1;   // null dates go last
        if (other.startDate == null) return -1;
        return this.startDate.compareTo(other.startDate);
    }


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