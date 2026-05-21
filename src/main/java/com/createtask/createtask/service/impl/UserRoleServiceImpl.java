package com.createtask.createtask.service.impl;

import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.exception.DuplicateRoleException;
import com.createtask.createtask.exception.RoleNotFoundException;
import com.createtask.createtask.repository.UserRoleRepository;
import com.createtask.createtask.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

/**
 * UserRoleServiceImpl provides the concrete business logic for all role operations.
 * Uses constructor injection for the repository dependency.
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Validates that the role name is unique before persisting.
     * Role names serve as human-readable identifiers in the system.
     */
    @Override
    public UserRole createRole(UserRole userRole) {

        // Prevent duplicate role ID
        if (userRoleRepository.existsById(userRole.getUserRoleID())) {
            throw new RuntimeException(
                    "Role ID already exists"
            );
        }

        // Prevent duplicate role name
        if (userRoleRepository.existsByRoleName(userRole.getRoleName())) {
            throw new DuplicateRoleException(userRole.getRoleName());
        }

        // Allow only alphabets and spaces in role name
        if (!userRole.getRoleName().matches("^[A-Za-z ]+$")) {
            throw new RuntimeException(
                    "Role name must contain only letters"
            );
        }

        return userRoleRepository.save(userRole);
    }

    @Override
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    /**
     * Returns all roles sorted by userRoleID in ascending order.
     */
    @Override
    public TreeSet<UserRole> getAllRolesSorted() {

        TreeSet<UserRole> sortedRoles = new TreeSet<>(
                (r1, r2) -> Integer.compare(r1.getUserRoleID(), r2.getUserRoleID())
        );

        sortedRoles.addAll(userRoleRepository.findAll());

        return sortedRoles;
    }

    /**
     * Used by UserRolesServiceImpl to validate role existence
     * before performing any assignment or removal operation.
     */
    @Override
    public UserRole getRoleById(Integer roleId) {
        return userRoleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));
    }
}