package com.createtask.createtask.dto.response;

/**
 * UserResponseDTO is the output object returned to the client.
 * Password is intentionally excluded to prevent sensitive data exposure in API responses.
 */
public class UserResponseDTO {

    private Integer userID;
    private String username;
    private String email;
    private String fullName;

    public UserResponseDTO() {}

    /** Convenience constructor used in the controller to map from entity to DTO. */
    public UserResponseDTO(Integer userID, String username, String email, String fullName) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
    }

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}