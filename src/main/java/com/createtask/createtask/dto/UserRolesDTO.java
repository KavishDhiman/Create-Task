package com.createtask.createtask.dto;

import jakarta.validation.constraints.NotNull;

public class UserRolesDTO {

    @NotNull(message = "UserID must not be null")
    private Integer userID;

    @NotNull(message = "UserRoleID must not be null")
    private Integer userRoleID;

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public Integer getUserRoleID() { return userRoleID; }
    public void setUserRoleID(Integer userRoleID) { this.userRoleID = userRoleID; }
}