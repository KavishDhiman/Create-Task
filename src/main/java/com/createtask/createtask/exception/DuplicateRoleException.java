package com.createtask.createtask.exception;

/**
 * Thrown when a role creation uses a role name that already exists in the database.
 * Mapped to HTTP 409 by GlobalExceptionHandler.
 */
public class DuplicateRoleException extends RuntimeException {

    private final String roleName;

    public DuplicateRoleException(String roleName) {
        super("Role already exists with name: " + roleName);
        this.roleName = roleName;
    }

    public String getRoleName() { return roleName; }
}