package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "UserRole")
public class UserRole {

    @Id
    @Column(name = "UserRoleID")
    private int userRoleID;

    @Column(name = "RoleName", nullable = false, length = 255)
    private String roleName;
}
