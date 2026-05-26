package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * DTO for creating or updating a Task.
 *
 * Validation lives HERE — on the DTO, not the Entity.
 *
 * Reasoning:
 *   - The Entity is a JPA/DB mapping concern. Mixing @NotBlank there
 *     couples your database layer to your API contract.
 *   - The DTO is the API contract. Validate at the boundary where
 *     untrusted input enters the system.
 *   - The Controller uses @Valid on the @RequestBody DTO, so Spring
 *     fires these constraints before the service is ever called.
 *   - The Entity can keep @Column(nullable=false) as a DB-level safety
 *     net, but @NotBlank / @NotNull on the entity are redundant and
 *     confusing — remove them from Task.java and Category.java.
 */
public class TaskRequestDTO {

    /**
     * taskID — supplied by the client (manual ID, no auto-increment).
     *
     * @Positive rejects 0 and negatives. Null is rejected by @NotNull.
     * Use Integer (boxed) so @NotNull can fire; int primitives can't be null.
     */
    @NotNull(message = "Task ID is required")
    @Positive(message = "Task ID must be a positive number")
    private Integer taskID;

    /**
     * @NotBlank covers: null, "", "   " (blank string).
     * Plain @NotNull would allow "   " through — always prefer @NotBlank for strings.
     */
    @NotBlank(message = "Task name is required")
    private String taskName;

    /**
     * description is optional — no annotation needed.
     * If you want a max length, add @Size(max=1000).
     */
    @NotBlank(message = "Description is required")
    private String description;

    /**
     * dueDate is optional. If you want to require it, add @NotNull.
     * No @NotBlank — LocalDate is not a String.
     */
    private LocalDate dueDate;

    /**
     * @Pattern restricts to exactly the three allowed values.
     * @NotBlank added so an empty string also fails cleanly.
     *
     * Why @Pattern and not an Enum?
     *   Enums require a custom deserializer for friendly error messages.
     *   @Pattern gives a clear, immediate validation message.
     *   Switch to an Enum + @NotNull if you want compile-time safety later.
     */
    @NotBlank(message = "Priority is required")
    @Pattern(
            regexp = "High|Medium|Low",
            message = "Priority must be one of: High, Medium, Low"
    )
    private String priority;

    /**
     * Same pattern approach as priority.
     */
    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "Pending|In Progress|Completed",
            message = "Status must be one of: Pending, In Progress, Completed"
    )
    private String status;

    /**
     * projectID links to an existing Project entity.
     * @NotNull — must be provided.
     * @Positive — must be a real ID, not 0 or negative.
     *
     * Note: @Positive on Integer (boxed) works correctly.
     * Do NOT use int (primitive) here — @NotNull cannot fire on primitives.
     */
    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be a positive number")
    private Integer projectID;

    /**
     * userID — same rules as projectID.
     */
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Integer userID;

    // ── Constructors ──────────────────────────────────────────────────────────

    public TaskRequestDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Integer getTaskID() { return taskID; }
    public void setTaskID(Integer taskID) { this.taskID = taskID; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProjectID() { return projectID; }
    public void setProjectID(Integer projectID) { this.projectID = projectID; }

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }
}