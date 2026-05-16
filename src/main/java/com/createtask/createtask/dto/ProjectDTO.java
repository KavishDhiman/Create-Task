package com.createtask.createtask.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ProjectDTO {

    private Integer projectID;

    @NotBlank(message = "Project name must not be blank. Please provide a meaningful name for the project.")
    @Size(max = 255, message = "Project name must not exceed 255 characters. Please shorten the project name.")
    private String projectName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters. Please provide a concise project description.")
    private String description;

    @NotNull(message = "Start date must not be null. Please provide a valid start date for the project.")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "User ID must not be null. Every project must be associated with a valid registered user.")
    private Integer userID;

    public ProjectDTO() {}

    public ProjectDTO(Integer projectID, String projectName, String description,
                      LocalDate startDate, LocalDate endDate, Integer userID) {
        this.projectID   = projectID;
        this.projectName = projectName;
        this.description = description;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.userID      = userID;
    }

    public Integer getProjectID()      { return projectID; }
    public String getProjectName()     { return projectName; }
    public String getDescription()     { return description; }
    public LocalDate getStartDate()    { return startDate; }
    public LocalDate getEndDate()      { return endDate; }
    public Integer getUserID()         { return userID; }

    public void setProjectID(Integer projectID)        { this.projectID = projectID; }
    public void setProjectName(String projectName)     { this.projectName = projectName; }
    public void setDescription(String description)     { this.description = description; }
    public void setStartDate(LocalDate startDate)      { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate)          { this.endDate = endDate; }
    public void setUserID(Integer userID)              { this.userID = userID; }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "projectID=" + projectID +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", userID=" + userID +
                '}';
    }
}