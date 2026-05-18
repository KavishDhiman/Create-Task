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

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserRolesController exposes REST endpoints for managing the
 * many-to-many relationship between users and roles.
 * Endpoints are nested under /api/v1/users/{userId}/roles
 * as per the API specification.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User-Role Mapping", description = "APIs for assigning and removing roles from users")
public class UserRolesController {

    private final UserRolesService userRolesService;

    public UserRolesController(UserRolesService userRolesService) {
        this.userRolesService = userRolesService;
    }

    /** Converts a UserRole entity to a response DTO for API output. */
    private UserRoleResponseDTO toResponse(UserRole role) {
        return new UserRoleResponseDTO(role.getUserRoleID(), role.getRoleName());
    }

    @Operation(summary = "Assign a role to a user")
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> assignRole(
            @PathVariable Integer userId,
            @PathVariable Integer roleId) {
        UserRoles mapping = userRolesService.assignRoleToUser(userId, roleId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Role " + roleId + " assigned to User " + userId + " successfully.");
    }

    /**
     * Calls removeRoleFromUser which returns true on success.
     * Returns a confirmation message to the client with HTTP 200.
     */
    @Operation(summary = "Remove a role from a user")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<String> removeRole(
            @PathVariable Integer userId,
            @PathVariable Integer roleId) {
        boolean removed = userRolesService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok("Role " + roleId + " removed from User " + userId + " successfully. Status: " + removed);
    }

    @Operation(summary = "Get all roles assigned to a user")
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<UserRoleResponseDTO>> getRolesOfUser(@PathVariable Integer userId) {
        List<UserRoleResponseDTO> roles = userRolesService.getRolesOfUser(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
}