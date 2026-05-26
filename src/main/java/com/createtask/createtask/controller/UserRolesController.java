package com.createtask.createtask.controller;

import com.createtask.createtask.dto.response.UserRoleResponseDTO;
import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.entity.UserRoles;
import com.createtask.createtask.service.UserRolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@RestController // Marks this class as REST controller
@RequestMapping("/api/v1/users") // Base URL mapping for user-role APIs
@Tag(name = "User-Role Mapping", description = "APIs for assigning and removing roles from users") // Swagger API documentation tag
@Validated
public class UserRolesController {

    private final UserRolesService userRolesService; // Service dependency for user-role mapping operations

    // Constructor injection for UserRolesService
    public UserRolesController(UserRolesService userRolesService) {

        this.userRolesService = userRolesService; // Assigns UserRolesService object
    }

    // Converts UserRole entity into response DTO
    private UserRoleResponseDTO toResponse(UserRole role) {

        return new UserRoleResponseDTO( // Returns response DTO object
                role.getUserRoleID(),
                role.getRoleName()
        );
    }

    @Operation(summary = "Assign a role to a user") // Swagger operation summary
    @PostMapping("/{userId}/roles/{roleId}") // Maps POST request with path variables
    public ResponseEntity<String> assignRole(

            @PathVariable @Positive(message = "User ID must be positive")  Integer userId, // Receives user ID from URL

            @PathVariable @Positive(message = "Role ID must be positive")  Integer roleId) { // Receives role ID from URL

        UserRoles mapping = userRolesService.assignRoleToUser(userId, roleId); // Assigns role to user

        return ResponseEntity.status(HttpStatus.CREATED) // Sets HTTP status 201
                .body("Role " + roleId + " assigned to User " + userId + " successfully."); // Returns success message
    }

    @Operation(summary = "Remove a role from a user") // Swagger operation summary
    @DeleteMapping("/{userId}/roles/{roleId}") // Maps DELETE request
    public ResponseEntity<String> removeRole(

            @PathVariable @Positive(message = "User ID must be positive") Integer userId, // Receives user ID from URL

            @PathVariable @Positive(message = "Role ID must be positive") Integer roleId) { // Receives role ID from URL

        boolean removed = userRolesService.removeRoleFromUser(userId, roleId); // Removes role mapping

        return ResponseEntity.ok( // Returns success response
                "Role " + roleId + " removed from User " + userId + " successfully. Status: " + removed
        );
    }

    @Operation(summary = "Get all roles assigned to a user") // Swagger operation summary
    @GetMapping("/{userId}/roles") // Maps GET request
    public ResponseEntity<List<UserRoleResponseDTO>> getRolesOfUser(

            @PathVariable @Positive(message = "User ID must be positive") Integer userId) { // Receives user ID from URL

        List<UserRoleResponseDTO> roles = userRolesService.getRolesOfUser(userId) // Retrieves assigned roles
                .stream() // Converts collection into stream
                .map(this::toResponse) // Converts entity into DTO
                .collect(Collectors.toList()); // Converts stream into list

        return ResponseEntity.ok(roles); // Returns response list
    }
}