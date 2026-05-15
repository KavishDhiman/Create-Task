package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "UserRole")
public class UserRole implements Comparable<UserRole> {

    @Id
    @Column(name = "UserRoleID")
    @NotNull(message = "UserRoleID must not be null")
    private Integer userRoleID;

    @Column(name = "RoleName", nullable = false, length = 255)
    @NotBlank(message = "RoleName must not be blank")
    @Size(max = 255, message = "RoleName must not exceed 255 characters")
    private String roleName;

    public Integer getUserRoleID() { return userRoleID; }
    public void setUserRoleID(Integer userRoleID) { this.userRoleID = userRoleID; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(userRoleID, userRole.userRoleID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRoleID);
    }

    @Override
    public int compareTo(UserRole other) {
        return this.roleName.compareTo(other.roleName);
    }

    @Override
    public String toString() {
        return "UserRole{userRoleID=" + userRoleID + ", roleName='" + roleName + "'}";
    }
}