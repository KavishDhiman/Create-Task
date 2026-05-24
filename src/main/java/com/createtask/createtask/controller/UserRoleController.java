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

@RestController // Marks this class as REST controller
@RequestMapping("/api/v1/roles") // Base URL mapping for role APIs
@Tag(name = "Role Management", description = "APIs for creating and listing user roles") // Swagger API documentation tag
public class UserRoleController {

    private final UserRoleService userRoleService; // Service dependency for role operations

    // Constructor injection for UserRoleService
    public UserRoleController(UserRoleService userRoleService) {

        this.userRoleService = userRoleService; // Assigns UserRoleService object
    }

    // Converts request DTO into entity object
    private UserRole toEntity(UserRoleRequestDTO dto) {

        UserRole role = new UserRole(); // Creates UserRole object

        role.setUserRoleID(dto.getUserRoleID()); // Sets role ID
        role.setRoleName(dto.getRoleName()); // Sets role name

        return role; // Returns entity object
    }

    // Converts entity object into response DTO
    private UserRoleResponseDTO toResponse(UserRole role) {

        return new UserRoleResponseDTO( // Returns response DTO object
                role.getUserRoleID(),
                role.getRoleName()
        );
    }

    @Operation(summary = "Create a new role") // Swagger operation summary
    @PostMapping // Maps POST request
    public ResponseEntity<UserRoleResponseDTO> createRole(

            @Valid @RequestBody UserRoleRequestDTO dto) { // Validates and receives request body

        UserRole saved = userRoleService.createRole(toEntity(dto)); // Saves role into database

        return ResponseEntity.status(HttpStatus.CREATED) // Sets HTTP status 201
                .body(toResponse(saved)); // Returns created role response
    }

    @Operation(summary = "List all roles sorted alphabetically by role name") // Swagger operation summary
    @GetMapping // Maps GET request
    public ResponseEntity<List<UserRoleResponseDTO>> getAllRoles() {

        List<UserRoleResponseDTO> response = userRoleService.getAllRolesSorted() // Retrieves sorted roles
                .stream() // Converts collection into stream
                .map(this::toResponse) // Converts entity into DTO
                .collect(Collectors.toList()); // Converts stream into list

        return ResponseEntity.ok(response); // Returns response list
    }
}