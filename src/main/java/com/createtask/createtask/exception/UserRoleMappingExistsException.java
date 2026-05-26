package com.createtask.createtask.exception;

public class UserRoleMappingExistsException extends RuntimeException {

    public UserRoleMappingExistsException(Integer userId) {

        super("Cannot delete user because role mappings exist for User ID: " + userId);
    }
}