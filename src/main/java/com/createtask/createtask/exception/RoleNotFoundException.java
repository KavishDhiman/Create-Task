package com.createtask.createtask.exception;

// Custom exception for role not found cases
public class RoleNotFoundException extends RuntimeException {

    private final Integer roleId;

    // Constructor for role ID
    public RoleNotFoundException(Integer roleId) {

        super("Role not found with ID: " + roleId);

        this.roleId = roleId;
    }

    // Constructor for custom message
    public RoleNotFoundException(String message) {

        super(message);

        this.roleId = null;
    }

    // Getter method for roleId
    public Integer getRoleId() {
        return roleId;
    }
}