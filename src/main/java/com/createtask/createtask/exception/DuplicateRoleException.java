package com.createtask.createtask.exception;

// Custom exception for duplicate role cases
public class DuplicateRoleException extends RuntimeException {

    private final String roleName; // Stores duplicate role name

    // Constructor for initializing exception
    public DuplicateRoleException(String roleName) {
        super("Role already exists with name: " + roleName); // Passes error message to parent exception class
        this.roleName = roleName; // Assigns duplicate role name
    }

    // Getter method for roleName
    public String getRoleName() {
        return roleName; // Returns duplicate role name
    }
}