package com.createtask.createtask.dto.response;

/**
 * Response DTO for the User Productivity Report endpoint.
 * Carries aggregated task statistics per user — no entity fields exposed directly.
 */
public class UserProductivityDTO {

    private Integer userId;
    private String userName;
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;

    /** Calculated as (completedTasks / totalTasks) * 100, rounded to 2 decimal places. */
    private double completionRate;

    public UserProductivityDTO() {}

    public UserProductivityDTO(Integer userId, String userName,
                               long totalTasks, long completedTasks,
                               long pendingTasks, double completionRate) {
        this.userId = userId;
        this.userName = userName;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.completionRate = completionRate;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }

    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }

    public long getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(long pendingTasks) { this.pendingTasks = pendingTasks; }

    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
}