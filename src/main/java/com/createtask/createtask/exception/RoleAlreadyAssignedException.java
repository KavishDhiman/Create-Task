package com.createtask.createtask.exception;

/**
 * Thrown when a role is assigned to a user who already has that role
 * in the UserRoles junction table.
 * Mapped to HTTP 409 by GlobalExceptionHandler.
 */
public class RoleAlreadyAssignedException extends RuntimeException {

    private final Integer userId;
    private final Integer roleId;

    public RoleAlreadyAssignedException(Integer userId, Integer roleId) {
        super("Role with ID " + roleId + " is already assigned to User with ID " + userId);
        this.userId = userId;
        this.roleId = roleId;
    }

    public Integer getUserId() { return userId; }
    public Integer getRoleId() { return roleId; }
}