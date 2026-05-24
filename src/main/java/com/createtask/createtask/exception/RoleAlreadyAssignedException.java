package com.createtask.createtask.exception;

// Custom exception for duplicate role assignment cases
public class RoleAlreadyAssignedException extends RuntimeException {

    private final Integer userId; // Stores user ID
    private final Integer roleId; // Stores role ID

    // Constructor for initializing exception
    public RoleAlreadyAssignedException(Integer userId, Integer roleId) {
        // Passes error message to parent exception class
        super("Role with ID " + roleId + " is already assigned to User with ID " + userId);
        this.userId = userId; // Assigns user ID value
        this.roleId = roleId; // Assigns role ID value
    }

    // Getter method for userId
    public Integer getUserId() {
        return userId; // Returns user ID
    }

    // Getter method for roleId
    public Integer getRoleId() {
        return roleId; // Returns role ID
    }
}