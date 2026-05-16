package com.createtask.createtask.controller;

import com.createtask.createtask.dto.request.UserRoleRequestDTO;
import com.createtask.createtask.dto.response.UserRoleResponseDTO;
import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserRoleController exposes REST endpoints for role management.
 * All endpoints follow the /api/v1/roles base path as per the API specification.
 */
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Role Management", description = "APIs for creating and listing user roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /** Converts UserRoleRequestDTO to UserRole entity for service processing. */
    private UserRole toEntity(UserRoleRequestDTO dto) {
        UserRole role = new UserRole();
        role.setUserRoleID(dto.getUserRoleID());
        role.setRoleName(dto.getRoleName());
        return role;
    }

    /** Converts UserRole entity to UserRoleResponseDTO for the API response. */
    private UserRoleResponseDTO toResponse(UserRole role) {
        return new UserRoleResponseDTO(role.getUserRoleID(), role.getRoleName());
    }

    @Operation(summary = "Create a new role")
    @PostMapping
    public ResponseEntity<UserRoleResponseDTO> createRole(@Valid @RequestBody UserRoleRequestDTO dto) {
        UserRole saved = userRoleService.createRole(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @Operation(summary = "List all roles sorted alphabetically by role name")
    @GetMapping
    public ResponseEntity<List<UserRoleResponseDTO>> getAllRoles() {
        List<UserRoleResponseDTO> response = userRoleService.getAllRolesSorted()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}