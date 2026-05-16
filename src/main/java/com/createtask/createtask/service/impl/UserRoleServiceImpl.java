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
        if (userRoleRepository.existsByRoleName(userRole.getRoleName())) {
            throw new DuplicateRoleException(userRole.getRoleName());
        }
        return userRoleRepository.save(userRole);
    }

    @Override
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    /**
     * TreeSet automatically calls UserRole's compareTo() on each insertion,
     * producing roles sorted alphabetically by roleName.
     */
    @Override
    public TreeSet<UserRole> getAllRolesSorted() {
        return new TreeSet<>(userRoleRepository.findAll());
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