package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO class for receiving user request data
public class UserRequestDTO {

    @NotNull(message = "UserID must not be null") // Validates non-null user ID
    private Integer userID; // Stores user ID

    @NotBlank(message = "Username must not be blank") // Prevents blank username
    @Size(max = 255, message = "Username must not exceed 255 characters") // Validates username length
    private String username; // Stores username

    @NotBlank(message = "Password must not be blank") // Prevents blank password
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters") // Validates password length
    private String password; // Stores password

    @NotBlank(message = "Email must not be blank") // Prevents blank email
    @Email(message = "Email should be valid") // Validates email format
    @Size(max = 255, message = "Email must not exceed 255 characters") // Validates email length
    private String email; // Stores email

    @NotBlank(message = "FullName must not be blank") // Prevents blank full name
    @Size(max = 255, message = "FullName must not exceed 255 characters") // Validates full name length
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
}