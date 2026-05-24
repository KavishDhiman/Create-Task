package com.createtask.createtask.dto.response;

// DTO class for sending role response data
public class UserRoleResponseDTO {

    private Integer userRoleID; // Stores role ID
    private String roleName; // Stores role name

    // Default constructor
    public UserRoleResponseDTO() {}

    // Parameterized constructor for initializing all fields
    public UserRoleResponseDTO(Integer userRoleID, String roleName) {
        this.userRoleID = userRoleID; // Assigns role ID value
        this.roleName = roleName; // Assigns role name value
    }

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