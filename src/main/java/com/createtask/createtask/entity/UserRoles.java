package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "UserRoles")
public class UserRoles implements Comparable<UserRoles> {

    @EmbeddedId
    private UserRolesId id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "UserID")
    @NotNull(message = "User must not be null")
    private User user;

    @ManyToOne
    @MapsId("userRoleID")
    @JoinColumn(name = "UserRoleID")
    @NotNull(message = "UserRole must not be null")
    private UserRole userRole;

    public UserRolesId getId() { return id; }
    public void setId(UserRolesId id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoles)) return false;
        UserRoles userRoles = (UserRoles) o;
        return Objects.equals(id, userRoles.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(UserRoles other) {
        int userCompare = Integer.compare(this.id.getUserID(), other.id.getUserID());
        if (userCompare != 0) return userCompare;
        return Integer.compare(this.id.getUserRoleID(), other.id.getUserRoleID());
    }

    @Override
    public String toString() {
        return "UserRoles{userID=" + id.getUserID() + ", userRoleID=" + id.getUserRoleID() + "}";
    }

    @Embeddable
    public static class UserRolesId implements Serializable {

        @Column(name = "UserID")
        @NotNull(message = "UserID must not be null")
        private Integer userID;

        @Column(name = "UserRoleID")
        @NotNull(message = "UserRoleID must not be null")
        private Integer userRoleID;

        public Integer getUserID() { return userID; }
        public void setUserID(Integer userID) { this.userID = userID; }

        public Integer getUserRoleID() { return userRoleID; }
        public void setUserRoleID(Integer userRoleID) { this.userRoleID = userRoleID; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserRolesId)) return false;
            UserRolesId that = (UserRolesId) o;
            return Objects.equals(userID, that.userID) &&
                    Objects.equals(userRoleID, that.userRoleID);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userID, userRoleID);
        }
    }
}