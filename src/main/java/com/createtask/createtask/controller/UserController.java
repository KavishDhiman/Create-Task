package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.UserRequestDTO;
import com.createtask.createtask.dto.response.UserResponseDTO;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.service.UserService;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController // Marks this class as REST controller
@RequestMapping("/api/v1/users") // Base URL mapping for user APIs
@Tag( // Swagger API documentation tag
        name = "User Management",
        description = "APIs for creating, retrieving, updating and deleting users"
)
@Validated
public class UserController {

    private final UserService userService; // Service dependency for user operations

    // Constructor injection for UserService
    public UserController(UserService userService) {

        this.userService = userService; // Assigns UserService object
    }

    // Converts request DTO into entity object
    private AppUser toEntity(UserRequestDTO dto) {

        AppUser user = new AppUser(); // Creates AppUser object

        user.setUserID(dto.getUserID()); // Sets user ID
        user.setUsername(dto.getUsername()); // Sets username
        user.setPassword(dto.getPassword()); // Sets password
        user.setEmail(dto.getEmail()); // Sets email
        user.setFullName(dto.getFullName()); // Sets full name

        return user; // Returns entity object
    }

    // Converts entity object into response DTO
    private UserResponseDTO toResponse(AppUser user) {

        return new UserResponseDTO( // Returns response DTO object
                user.getUserID(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }

    // API for creating new user
    @Operation(summary = "Create a new user") // Swagger operation summary
    @PostMapping // Maps POST request
    public ResponseEntity<UserResponseDTO> createUser(

            @Valid @RequestBody UserRequestDTO dto) { // Validates and receives request body

        AppUser saved = userService.createUser(toEntity(dto)); // Saves user into database

        return ResponseEntity
                .status(HttpStatus.CREATED) // Sets HTTP status 201
                .body(toResponse(saved)); // Returns created user response
    }

    // API for retrieving user by ID
    @Operation(summary = "Get user by ID") // Swagger operation summary
    @GetMapping("/{userId}") // Maps GET request with path variable
    public ResponseEntity<UserResponseDTO> getUserById(

            @PathVariable @Positive(message = "User ID must be positive") Integer userId) { // Receives user ID from URL

        return ResponseEntity.ok( // Returns success response
                toResponse(userService.getUserById(userId))
        );
    }

    // API for retrieving all users
    @Operation(summary = "List all users sorted by userID") // Swagger operation summary
    @GetMapping // Maps GET request
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> response = // Stores response DTO list
                userService.getAllUsersSorted()
                        .stream() // Converts collection into stream
                        .map(this::toResponse) // Converts entity into DTO
                        .collect(Collectors.toList()); // Converts stream into list

        return ResponseEntity.ok(response); // Returns response list
    }

    // API for updating user
    @Operation(summary = "Update user profile") // Swagger operation summary
    @PutMapping("/{userId}") // Maps PUT request
    public ResponseEntity<UserResponseDTO> updateUser(

            @PathVariable @Positive(message = "User ID must be positive") Integer userId, // Receives user ID from URL

            @Valid @RequestBody UserRequestDTO dto) { // Validates request body

        AppUser updated = // Stores updated user object
                userService.updateUser(userId, toEntity(dto));

        return ResponseEntity.ok( // Returns updated response
                toResponse(updated)
        );
    }

    // API for deleting user
    @Operation(summary = "Delete a user by ID") // Swagger operation summary
    @DeleteMapping("/{userId}") // Maps DELETE request
    public ResponseEntity<String> deleteUser(

            @PathVariable @Positive(message = "User ID must be positive") Integer userId) { // Receives user ID from URL

        boolean deleted = // Stores deletion result
                userService.deleteUser(userId);

        return ResponseEntity.ok( // Returns deletion success message
                "User with ID " + userId +
                        " deleted successfully. Status: " + deleted
        );
    }

}