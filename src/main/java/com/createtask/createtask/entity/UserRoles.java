package com.createtask.createtask.entity;

import jakarta.persistence.*;
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
    private User user;

    @ManyToOne
    @MapsId("userRoleID")
    @JoinColumn(name = "UserRoleID")
    private UserRole userRole;

    @Embeddable
    @Getter
    @Setter
    public static class UserRolesId implements Serializable {

        @Column(name = "UserID")
        private int userID;

        @Column(name = "UserRoleID")
        private int userRoleID;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserRolesId)) return false;
            UserRolesId that = (UserRolesId) o;
            return userID == that.userID && userRoleID == that.userRoleID;
        }

        @Override
        public int hashCode() { return Objects.hash(userID, userRoleID); }
    }
}