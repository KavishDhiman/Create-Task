package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "User")
public class User implements Comparable<User> {

    @Id
    @Column(name = "UserID")
    @NotNull(message = "UserID must not be null")
    private Integer userID;

    @Column(name = "Username", nullable = false, length = 255)
    @NotBlank(message = "Username must not be blank")
    @Size(max = 255, message = "Username must not exceed 255 characters")
    private String username;

    @Column(name = "Password", nullable = false, length = 255)
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @Column(name = "Email", nullable = false, length = 255)
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Column(name = "FullName", nullable = false, length = 255)
    @NotBlank(message = "FullName must not be blank")
    @Size(max = 255, message = "FullName must not exceed 255 characters")
    private String fullName;

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }

    @Override
    public int compareTo(User other) {
        return Integer.compare(this.userID, other.userID);
    }

    @Override
    public String toString() {
        return "User{userID=" + userID + ", username='" + username + "', email='" + email + "'}";
    }
}