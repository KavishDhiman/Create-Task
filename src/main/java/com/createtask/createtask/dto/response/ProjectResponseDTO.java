package com.createtask.createtask.dto.response;
import java.time.LocalDate;
import java.util.Objects;

// Carries data going OUT to the client (GET/POST/PUT responses).
// We expose userName and userID so the client gets readable info without the full User object.
public class ProjectResponseDTO {

    private Integer projectID;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer userID;
    private String userName;   // human-readable owner name for the response

    // ==================== Constructors ====================
    public ProjectResponseDTO() {}

    public ProjectResponseDTO(Integer projectID, String projectName, String description,
                              LocalDate startDate, LocalDate endDate,
                              Integer userID, String userName) {
        this.projectID   = projectID;
        this.projectName = projectName;
        this.description = description;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.userID      = userID;
        this.userName    = userName;
    }

    // ==================== Getters ====================
    public Integer getProjectID()      { return projectID; }
    public String getProjectName()     { return projectName; }
    public String getDescription()     { return description; }
    public LocalDate getStartDate()    { return startDate; }
    public LocalDate getEndDate()      { return endDate; }
    public Integer getUserID()         { return userID; }
    public String getUserName()        { return userName; }

    // ==================== Setters ====================
    public void setProjectID(Integer projectID)        { this.projectID = projectID; }
    public void setProjectName(String projectName)     { this.projectName = projectName; }
    public void setDescription(String description)     { this.description = description; }
    public void setStartDate(LocalDate startDate)      { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate)          { this.endDate = endDate; }
    public void setUserID(Integer userID)              { this.userID = userID; }
    public void setUserName(String userName)           { this.userName = userName; }

    // ==================== toString ====================
    @Override
    public String toString() {
        return "ProjectResponseDTO{" +
                "projectID=" + projectID +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                '}';
    }
}