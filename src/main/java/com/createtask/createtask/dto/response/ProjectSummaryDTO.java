package com.createtask.createtask.dto.response;

// Response DTO for the project summary dashboard — carries aggregated task stats per project.
// Follows the same structure pattern as UserProductivityDTO for consistency.
public class ProjectSummaryDTO {

    private Integer projectId;
    private String projectName;
    private long totalTasks;
    private long completedTasks;
    private long inProgressTasks;
    private long pendingTasks;

    // Calculated as (completedTasks / totalTasks) * 100, rounded to 2 decimal places.
    private double completionPercentage;

    public ProjectSummaryDTO() {}

    public ProjectSummaryDTO(Integer projectId, String projectName,
                             long totalTasks, long completedTasks,
                             long inProgressTasks, long pendingTasks,
                             double completionPercentage) {
        this.projectId            = projectId;
        this.projectName          = projectName;
        this.totalTasks           = totalTasks;
        this.completedTasks       = completedTasks;
        this.inProgressTasks      = inProgressTasks;
        this.pendingTasks         = pendingTasks;
        this.completionPercentage = completionPercentage;
    }

    public Integer getProjectId()              { return projectId; }
    public void setProjectId(Integer projectId){ this.projectId = projectId; }

    public String getProjectName()                  { return projectName; }
    public void setProjectName(String projectName)  { this.projectName = projectName; }

    public long getTotalTasks()                { return totalTasks; }
    public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }

    public long getCompletedTasks()                    { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }

    public long getInProgressTasks()                       { return inProgressTasks; }
    public void setInProgressTasks(long inProgressTasks)   { this.inProgressTasks = inProgressTasks; }

    public long getPendingTasks()                    { return pendingTasks; }
    public void setPendingTasks(long pendingTasks)   { this.pendingTasks = pendingTasks; }

    public double getCompletionPercentage()                        { return completionPercentage; }
    public void setCompletionPercentage(double completionPercentage){ this.completionPercentage = completionPercentage; }
}