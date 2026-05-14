package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "UserID")
    private int userID;

    @Column(name = "Username", nullable = false, length = 255)
    private String username;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Column(name = "Email", nullable = false, length = 255)
    private String email;

    @Column(name = "FullName", nullable = false, length = 255)
    private String fullName;
}