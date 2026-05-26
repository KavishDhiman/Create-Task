package com.createtask.createtask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity // Marks this class as a database entity
@Table(name = "User") // Maps this entity to User table
public class AppUser implements Comparable<AppUser> { // Comparable allows sorting

    @Id // Marks primary key
    @Column(name = "UserID") // Maps field to UserID column
    @NotNull(message = "UserID must not be null") // Validates non-null ID
    @Positive(message = "UserID must be positive")
    private Integer userID; // Stores user ID

    @Column(name = "Username", nullable = false, length = 255) // Username column mapping
    @NotBlank(message = "Username must not be blank") // Prevents blank username
    @Size(max = 255, message = "Username must not exceed 255 characters") // Max length validation
    private String username; // Stores username

    @Column(name = "Password", nullable = false, length = 255) // Password column mapping
    @NotBlank(message = "Password must not be blank") // Prevents blank password
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters") // Password length validation
    private String password; // Stores password

    @Column(name = "Email", nullable = false, length = 255) // Email column mapping
    @NotBlank(message = "Email must not be blank") // Prevents blank email
    @Email(message = "Email should be valid") // Validates email format
    @Size(max = 255, message = "Email must not exceed 255 characters") // Max email length
    private String email; // Stores email

    @Column(name = "FullName", nullable = false, length = 255) // FullName column mapping
    @NotBlank(message = "FullName must not be blank") // Prevents blank fullname
    @Size(max = 255, message = "FullName must not exceed 255 characters") // Max fullname length
    private String fullName; // Stores full name

    // Getter method for userID
    public Integer getUserID() {
        return userID;
    }

    // Setter method for userID
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    // Getter method for username
    public String getUsername() {
        return username;
    }

    // Setter method for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter method for password
    public String getPassword() {
        return password;
    }

    // Setter method for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter method for email
    public String getEmail() {
        return email;
    }

    // Setter method for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter method for fullName
    public String getFullName() {
        return fullName;
    }

    // Setter method for fullName
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override // Overrides default equals method
    public boolean equals(Object o) {
        if (this == o) return true; // Checks same object reference
        if (!(o instanceof AppUser)) return false; // Checks object type
        AppUser user = (AppUser) o; // Typecasts Object to AppUser
        return Objects.equals(userID, user.userID); // Compares user IDs
    }

    @Override // Overrides default hashCode method
    public int hashCode() {
        return Objects.hash(userID); // Generates hash using userID
    }

    @Override // Overrides compareTo for sorting
    public int compareTo(AppUser other) {
        return Integer.compare(this.userID, other.userID); // Compares IDs for sorting
    }

    @Override // Overrides default toString method
    public String toString() {
        return "User{userID=" + userID + ", username='" + username + "', email='" + email + "'}"; // Returns readable object string
    }
}
