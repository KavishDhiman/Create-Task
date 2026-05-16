package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * UserRoleRequestDTO is the input object for creating a new role.
 * Validated by @Valid in the controller before reaching the service.
 */
public class UserRoleRequestDTO {

    @NotNull(message = "UserRoleID must not be null")
    private Integer userRoleID;

    @NotBlank(message = "RoleName must not be blank")
    @Size(max = 255, message = "RoleName must not exceed 255 characters")
    private String roleName;

    public Integer getUserRoleID() { return userRoleID; }
    public void setUserRoleID(Integer userRoleID) { this.userRoleID = userRoleID; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}