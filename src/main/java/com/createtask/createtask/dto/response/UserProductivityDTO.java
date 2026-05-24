package com.createtask.createtask.dto.response;

// DTO class for user productivity report response
public class UserProductivityDTO {

    private Integer userId; // Stores user ID
    private String userName; // Stores user name
    private long totalTasks; // Stores total number of tasks
    private long completedTasks; // Stores completed task count
    private long pendingTasks; // Stores pending task count
    private double completionRate; // Stores task completion percentage

    // Default constructor
    public UserProductivityDTO() {}

    // Parameterized constructor for initializing all fields
    public UserProductivityDTO(Integer userId, String userName,
                               long totalTasks, long completedTasks,
                               long pendingTasks, double completionRate) {

        this.userId = userId; // Assigns user ID value
        this.userName = userName; // Assigns user name value
        this.totalTasks = totalTasks; // Assigns total task count
        this.completedTasks = completedTasks; // Assigns completed task count
        this.pendingTasks = pendingTasks; // Assigns pending task count
        this.completionRate = completionRate; // Assigns completion rate value
    }

    // Getter method for userId
    public Integer getUserId() {
        return userId;
    }

    // Setter method for userId
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // Getter method for userName
    public String getUserName() {
        return userName;
    }

    // Setter method for userName
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter method for totalTasks
    public long getTotalTasks() {
        return totalTasks;
    }

    // Setter method for totalTasks
    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    // Getter method for completedTasks
    public long getCompletedTasks() {
        return completedTasks;
    }

    // Setter method for completedTasks
    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    // Getter method for pendingTasks
    public long getPendingTasks() {
        return pendingTasks;
    }

    // Setter method for pendingTasks
    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    // Getter method for completionRate
    public double getCompletionRate() {
        return completionRate;
    }

    // Setter method for completionRate
    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }
}