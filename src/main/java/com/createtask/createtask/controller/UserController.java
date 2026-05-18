package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.UserRequestDTO;
import com.createtask.createtask.dto.response.UserResponseDTO;
import com.createtask.createtask.entity.User;
import com.createtask.createtask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserController exposes REST endpoints for user management.
 * All endpoints follow the /api/v1/users base path as per the API specification.
 * @Valid triggers DTO validation before the request reaches the service layer.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for creating, retrieving, updating and deleting users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Converts a UserRequestDTO to a User entity for service layer processing. */
    private User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setUserID(dto.getUserID());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        return user;
    }

    /** Converts a User entity to a UserResponseDTO, excluding the password field. */
    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getUserID(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        User saved = userService.createUser(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(toResponse(userService.getUserById(userId)));
    }

    @Operation(summary = "List all users sorted by userID")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> response = userService.getAllUsersSorted()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer userId,
            @Valid @RequestBody UserRequestDTO dto) {
        User updated = userService.updateUser(userId, toEntity(dto));
        return ResponseEntity.ok(toResponse(updated));
    }

    /**
     * Calls deleteUser which returns true on success.
     * Returns a confirmation message to the client with HTTP 200.
     */
    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        boolean deleted = userService.deleteUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " deleted successfully. Status: " + deleted);
    }
}