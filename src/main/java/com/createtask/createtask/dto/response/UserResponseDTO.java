package com.createtask.createtask.dto.response;

// DTO class for sending user response data
public class UserResponseDTO {

    private Integer userID; // Stores user ID
    private String username; // Stores username
    private String email; // Stores email
    private String fullName; // Stores full name

    // Default constructor
    public UserResponseDTO() {}

    // Parameterized constructor for initializing all fields
    public UserResponseDTO(Integer userID, String username, String email, String fullName) {
        this.userID = userID; // Assigns userID value
        this.username = username; // Assigns username value
        this.email = email; // Assigns email value
        this.fullName = fullName; // Assigns fullName value
    }

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