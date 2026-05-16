package com.createtask.createtask.exception;

/**
 * Thrown when a role lookup by ID finds no matching record,
 * or when a role mapping being removed does not exist.
 * Mapped to HTTP 404 by GlobalExceptionHandler.
 */
public class RoleNotFoundException extends RuntimeException {

    private final Integer roleId;

    public RoleNotFoundException(Integer roleId) {
        super("Role not found with ID: " + roleId);
        this.roleId = roleId;
    }

    public Integer getRoleId() { return roleId; }
}