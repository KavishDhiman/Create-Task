package com.createtask.createtask.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * UserRequestDTO is the input object received from the client for
 * create (POST) and update (PUT) user operations.
 * Validations here are enforced by @Valid in the controller before
 * the request reaches the service layer.
 */
public class UserRequestDTO {

    /** Required for manual ID assignment as per the project DB design. */
    @NotNull(message = "UserID must not be null")
    private Integer userID;

    @NotBlank(message = "Username must not be blank")
    @Size(max = 255, message = "Username must not exceed 255 characters")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

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
}
