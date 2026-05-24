package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity // Marks this class as a database entity
@Table(name = "UserRole") // Maps entity to UserRole table
public class UserRole implements Comparable<UserRole> { // Enables sorting of UserRole objects

    @Id // Marks primary key
    @Column(name = "UserRoleID") // Maps field to UserRoleID column
    @NotNull(message = "UserRoleID must not be null") // Validates non-null ID
    private Integer userRoleID; // Stores role ID

    @Column(name = "RoleName", nullable = false, length = 255) // Maps RoleName column
    @NotBlank(message = "RoleName must not be blank") // Prevents blank role name
    @Size(max = 255, message = "RoleName must not exceed 255 characters") // Validates max length
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

    @Override // Overrides default equals method
    public boolean equals(Object o) {
        if (this == o) return true; // Checks same object reference
        if (!(o instanceof UserRole)) return false; // Checks object type
        UserRole userRole = (UserRole) o; // Typecasts Object to UserRole
        return Objects.equals(userRoleID, userRole.userRoleID); // Compares role IDs
    }

    @Override // Overrides default hashCode method
    public int hashCode() {
        return Objects.hash(userRoleID); // Generates hash using role ID
    }

    @Override // Overrides compareTo method for sorting
    public int compareTo(UserRole other) {
        return this.roleName.compareTo(other.roleName); // Compares role names alphabetically
    }

    @Override // Overrides default toString method
    public String toString() {
        return "UserRole{userRoleID=" + userRoleID + ", roleName='" + roleName + "'}"; // Returns readable string output
    }
}
