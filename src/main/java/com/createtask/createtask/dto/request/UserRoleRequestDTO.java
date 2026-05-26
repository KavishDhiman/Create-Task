package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

// DTO class for receiving role request data
public class UserRoleRequestDTO {

    @NotNull(message = "UserRoleID must not be null") // Validates non-null role ID
    @Positive(message = "UserRoleID must be positive")
    private Integer userRoleID; // Stores role ID

    @NotBlank(message = "RoleName must not be blank") // Prevents blank role name
    @Size(max = 255, message = "RoleName must not exceed 255 characters") // Validates role name length
    private String roleName; // Stores role name

    // Getter method for userRoleID
    public Integer getUserRoleID() {
        return userRoleID;
    }

    // Setter method for userRoleID
    public void setUserRoleID(Integer userRoleID) {
        this.userRoleID = userRoleID;
    }

    // Getter method for roleName
    public String getRoleName() {
        return roleName;
    }

    // Setter method for roleName
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}