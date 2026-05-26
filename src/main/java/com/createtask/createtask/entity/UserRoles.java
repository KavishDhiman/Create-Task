package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Objects;
@Entity // Marks this class as a database entity
@Table(name = "UserRoles") // Maps entity to UserRoles table
public class UserRoles implements Comparable<UserRoles> { // Enables sorting of UserRoles objects

    @EmbeddedId // Marks composite primary key
    private UserRolesId id; // Stores composite key object

    @ManyToOne // Defines many-to-one relationship with AppUser
    @MapsId("userID") // Maps userID from composite key
    @JoinColumn(name = "UserID") // Maps UserID foreign key column
    @NotNull(message = "User must not be null") // Validates non-null user
    private AppUser user; // Stores AppUser object reference

    @ManyToOne // Defines many-to-one relationship with UserRole
    @MapsId("userRoleID") // Maps userRoleID from composite key
    @JoinColumn(name = "UserRoleID") // Maps UserRoleID foreign key column
    @NotNull(message = "UserRole must not be null") // Validates non-null role
    private UserRole userRole; // Stores UserRole object reference

    // Getter method for composite ID
    public UserRolesId getId() {
        return id;
    }

    // Setter method for composite ID
    public void setId(UserRolesId id) {
        this.id = id;
    }

    // Getter method for user object
    public AppUser getUser() {
        return user;
    }

    // Setter method for user object
    public void setUser(AppUser user) {
        this.user = user;
    }

    // Getter method for role object
    public UserRole getUserRole() {
        return userRole;
    }

    // Setter method for role object
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override // Overrides default equals method
    public boolean equals(Object o) {
        if (this == o) return true; // Checks same object reference
        if (!(o instanceof UserRoles)) return false; // Checks object type
        UserRoles userRoles = (UserRoles) o; // Typecasts Object to UserRoles
        return Objects.equals(id, userRoles.id); // Compares composite IDs
    }

    @Override // Overrides default hashCode method
    public int hashCode() {
        return Objects.hash(id); // Generates hash using composite ID
    }

    @Override // Overrides compareTo method for sorting
    public int compareTo(UserRoles other) {
        int userCompare = Integer.compare(this.id.getUserID(), other.id.getUserID()); // Compares user IDs first
        if (userCompare != 0) return userCompare; // Returns if user IDs differ
        return Integer.compare(this.id.getUserRoleID(), other.id.getUserRoleID()); // Compares role IDs next
    }

    @Override // Overrides default toString method
    public String toString() {
        return "UserRoles{userID=" + id.getUserID() + ", userRoleID=" + id.getUserRoleID() + "}"; // Returns readable string
    }

    @Embeddable // Marks this class as embeddable composite key class
    public static class UserRolesId implements Serializable { // Composite key class implementing Serializable

        @Column(name = "UserID") // Maps UserID column
        @NotNull(message = "UserID must not be null") // Validates non-null userID
        @Positive(message = "UserID must be positive")
        private Integer userID; // Stores user ID

        @Column(name = "UserRoleID") // Maps UserRoleID column
        @NotNull(message = "UserRoleID must not be null") // Validates non-null role ID
        @Positive(message = "UserRoleID must be positive")
        private Integer userRoleID; // Stores role ID

        // Getter method for userID
        public Integer getUserID() {
            return userID;
        }

        // Setter method for userID
        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        // Getter method for userRoleID
        public Integer getUserRoleID() {
            return userRoleID;
        }

        // Setter method for userRoleID
        public void setUserRoleID(Integer userRoleID) {
            this.userRoleID = userRoleID;
        }

        @Override // Overrides default equals method
        public boolean equals(Object o) {
            if (this == o) return true; // Checks same object reference
            if (!(o instanceof UserRolesId)) return false; // Checks object type
            UserRolesId that = (UserRolesId) o; // Typecasts Object to UserRolesId
            return Objects.equals(userID, that.userID) && // Compares user IDs
                    Objects.equals(userRoleID, that.userRoleID); // Compares role IDs
        }

        @Override // Overrides default hashCode method
        public int hashCode() {
            return Objects.hash(userID, userRoleID); // Generates hash using both IDs
        }
    }
}
