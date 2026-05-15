package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "UserRoles")
public class UserRoles {

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

    @Embeddable
    @Getter
    @Setter
    public static class UserRolesId implements Serializable {

        @Column(name = "UserID")
        @NotNull(message = "UserID must not be null")
        private Integer userID;

        @Column(name = "UserRoleID")
        @NotNull(message = "UserRoleID must not be null")
        private Integer userRoleID;

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