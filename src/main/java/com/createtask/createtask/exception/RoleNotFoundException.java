package com.createtask.createtask.exception;

// Custom exception for role not found cases
public class RoleNotFoundException extends RuntimeException {

    private final Integer roleId; // Stores missing role ID

    // Constructor for initializing exception
    public RoleNotFoundException(Integer roleId) {
        super("Role not found with ID: " + roleId); // Passes error message to parent exception class
        this.roleId = roleId; // Assigns role ID value
    }

    // Getter method for roleId
    public Integer getRoleId() {
        return roleId; // Returns missing role ID
    }
}