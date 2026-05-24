package com.createtask.createtask.entity;

import jakarta.persistence.*; // JPA annotations for entity mapping
import jakarta.validation.constraints.NotBlank; // Ensures string fields are not null, empty, or whitespace
import jakarta.validation.constraints.NotNull; // Ensures fields are not null
import jakarta.validation.constraints.Size; // Restricts the max/min length of string fields
import java.time.LocalDate; // Used for storing project start and end dates
import java.util.Objects; // For null-safe equals and hashCode computation

@Entity // Marks this class as a JPA-managed persistent entity
@Table(name = "Project") // Maps this entity to the "Project" table in the database
public class Project implements Comparable<Project> {

    @Id // Marks this field as the primary key
    @Column(name = "ProjectID") // Maps to the ProjectID column in the DB
    @NotNull(message = "Project ID must not be null. Please provide a valid project identifier.")
    private Integer projectID; // Unique identifier for each project

    @NotBlank(message = "Project name must not be blank. Please provide a meaningful name for the project.")
    @Size(max = 255, message = "Project name must not exceed 255 characters. Please shorten the project name.")
    @Column(name = "ProjectName", nullable = false, length = 255) // Cannot be null and max length is 255
    private String projectName; // Name/title of the project

    @Column(name = "Description", columnDefinition = "TEXT") // Stored as TEXT in DB
    private String description; // Detailed description of the project

    @NotNull(message = "Start date must not be null. Please provide a valid start date for the project.")
    @Column(name = "StartDate") // Maps to StartDate column
    private LocalDate startDate; // Date when the project begins

    @Column(name = "EndDate") // Maps to EndDate column
    private LocalDate endDate; // Date when the project is expected to finish

    @NotNull(message = "User must not be null. Every project must be assigned to a valid registered user.")
    @ManyToOne // Many projects can belong to one user
    @JoinColumn(name = "UserID") // Foreign key linking to the User table
    private AppUser user; // The user who owns or manages this project

    // Default constructor required by JPA
    public Project() {}

    // Parameterized constructor for easier object creation
    public Project(Integer projectID, String projectName, String description,
                   LocalDate startDate, LocalDate endDate, AppUser user) {

        this.projectID = projectID;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    // Returns the unique project ID
    public Integer getProjectID() {
        return projectID;
    }

    // Returns the project name
    public String getProjectName() {
        return projectName;
    }

    // Returns the project description
    public String getDescription() {
        return description;
    }

    // Returns the project start date
    public LocalDate getStartDate() {
        return startDate;
    }

    // Returns the project end date
    public LocalDate getEndDate() {
        return endDate;
    }

    // Returns the user assigned to this project
    public AppUser getUser() {
        return user;
    }

    // Sets the unique project ID
    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    // Sets the project name
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // Sets the project description
    public void setDescription(String description) {
        this.description = description;
    }

    // Sets the project start date
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // Sets the project end date
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Sets the user assigned to this project
    public void setUser(AppUser user) {
        this.user = user;
    }

    // Two Project objects are equal if and only if they share the same projectID
    @Override
    public boolean equals(Object o) {

        if (this == o) return true; // Same object reference — trivially equal

        if (o == null || getClass() != o.getClass()) return false; // Null or different type — not equal

        Project project = (Project) o; // Safe cast after type check

        return Objects.equals(projectID, project.projectID); // Only the primary key determines equality
    }

    // hashCode must use the same field as equals() so Sets/Maps work correctly
    @Override
    public int hashCode() {

        return Objects.hash(projectID); // Generates a stable hash based on the primary key
    }

    // Used for sorting projects based on start date
    @Override
    public int compareTo(Project other) {

        if (this.startDate == null && other.startDate == null)
            return 0; // Both dates null — considered equal

        if (this.startDate == null)
            return 1; // Null dates go last

        if (other.startDate == null)
            return -1; // Non-null dates come first

        return this.startDate.compareTo(other.startDate); // Compare chronologically
    }

    // Returns a readable string representation of the Project object
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