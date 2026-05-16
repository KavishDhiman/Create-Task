package com.createtask.createtask.dto.response;

/**
 * UserRoleResponseDTO is the output object returned to the client for role-related responses.
 */
public class UserRoleResponseDTO {

    private Integer userRoleID;
    private String roleName;

    public UserRoleResponseDTO() {}

    public UserRoleResponseDTO(Integer userRoleID, String roleName) {
        this.userRoleID = userRoleID;
        this.roleName = roleName;
    }

    public Integer getUserRoleID() { return userRoleID; }
    public void setUserRoleID(Integer userRoleID) { this.userRoleID = userRoleID; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}