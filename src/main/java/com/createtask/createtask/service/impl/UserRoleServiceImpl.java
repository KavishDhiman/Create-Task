package com.createtask.createtask.service.impl;

import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.exception.DuplicateRoleException;
import com.createtask.createtask.exception.RoleNotFoundException;
import com.createtask.createtask.repository.UserRoleRepository;
import com.createtask.createtask.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

// Service implementation class for role-related business logic
@Service
public class UserRoleServiceImpl implements UserRoleService {

    // Repository dependency for database operations
    private final UserRoleRepository userRoleRepository;

    // Constructor injection for UserRoleRepository
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository; // Assigns repository object
    }

    // Creates a new role after validation
    @Override
    public UserRole createRole(UserRole userRole) {

        // Checks whether role ID already exists
        if (userRoleRepository.existsById(userRole.getUserRoleID())) {

            throw new RuntimeException(
                    "Role ID already exists"
            ); // Throws exception for duplicate role ID
        }

        // Checks whether role name already exists
        if (userRoleRepository.existsByRoleName(userRole.getRoleName())) {

            throw new DuplicateRoleException(userRole.getRoleName()); // Throws duplicate role name exception
        }

        // Validates role name contains only letters and spaces
        if (!userRole.getRoleName().matches("^[A-Za-z ]+$")) {

            throw new RuntimeException(
                    "Role name must contain only letters"
            ); // Throws exception for invalid role name
        }

        // Saves role into database
        return userRoleRepository.save(userRole);
    }

    // Retrieves all roles from database
    @Override
    public List<UserRole> getAllRoles() {

        // Returns all roles as list
        return userRoleRepository.findAll();
    }

    // Retrieves all roles sorted by role ID
    @Override
    public TreeSet<UserRole> getAllRolesSorted() {

        // Creates TreeSet with custom sorting logic
        TreeSet<UserRole> sortedRoles = new TreeSet<>(
                (r1, r2) -> Integer.compare(r1.getUserRoleID(), r2.getUserRoleID())
        );

        // Adds all roles into TreeSet
        sortedRoles.addAll(userRoleRepository.findAll());

        // Returns sorted roles
        return sortedRoles;
    }

    // Retrieves role by role ID
    @Override
    public UserRole getRoleById(Integer roleId) {

        // Finds role by ID or throws exception if absent
        return userRoleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
    }
}