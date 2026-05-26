package com.createtask.createtask.dto.request;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

// Carries data coming IN from the client (POST/PUT requests).
// We keep userID as a plain integer here — the service layer fetches the full User object.
public class ProjectRequestDTO {

    @NotNull(message = "Project ID must not be null. Please provide a valid project identifier.")
    @Positive(message = "Project ID must be greater than 0. Please provide a positive project identifier.")
    private Integer projectID;

    @NotBlank(message = "Project name must not be blank. Please provide a meaningful name for the project.")
    @Size(max = 255, message = "Project name must not exceed 255 characters. Please shorten the project name.")
    private String projectName;

    // Stores additional project-related information from the client.
    @Size(max = 1000, message = "Description must not exceed 1000 characters. Please provide a concise project description.")
    private String description;

    @NotNull(message = "Start date must not be null. Please provide a valid start date for the project.")
    private LocalDate startDate;

    private LocalDate endDate;

    /*
     * Only user ID is accepted from the client.
     * Full user object is fetched in the service layer.
     */
    @NotNull(message = "User ID must not be null. Every project must be associated with a valid registered user.")
    @Positive(message = "User ID must be greater than 0. Please provide a positive user identifier.")
    private Integer userID;

    public ProjectRequestDTO() {}

    public ProjectRequestDTO(Integer projectID, String projectName, String description,
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

    // Returns request details for debugging and logging purposes.
    @Override
    public String toString() {
        return "ProjectRequestDTO{" +
                "projectID=" + projectID +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", userID=" + userID +
                '}';
    }
}