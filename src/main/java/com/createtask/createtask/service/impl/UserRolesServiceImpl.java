package com.createtask.createtask.service.impl;

import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.entity.UserRoles;
import com.createtask.createtask.exception.RoleAlreadyAssignedException;
import com.createtask.createtask.exception.RoleNotFoundException;
import com.createtask.createtask.repository.UserRolesRepository;
import com.createtask.createtask.service.UserRoleService;
import com.createtask.createtask.service.UserRolesService;
import com.createtask.createtask.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Service implementation class for managing user-role mappings
@Service
public class UserRolesServiceImpl implements UserRolesService {

    // Repository dependency for UserRoles database operations
    private final UserRolesRepository userRolesRepository;

    // Service dependency for user validation and retrieval
    private final UserService userService;

    // Service dependency for role validation and retrieval
    private final UserRoleService userRoleService;

    // Constructor injection for dependencies
    public UserRolesServiceImpl(UserRolesRepository userRolesRepository,
                                UserService userService,
                                UserRoleService userRoleService) {

        this.userRolesRepository = userRolesRepository; // Assigns UserRolesRepository object
        this.userService = userService; // Assigns UserService object
        this.userRoleService = userRoleService; // Assigns UserRoleService object
    }

    // Assigns a role to a user
    @Override
    public UserRoles assignRoleToUser(Integer userId, Integer roleId) {

        // Retrieves user by ID
        AppUser user = userService.getUserById(userId);

        // Retrieves role by ID
        UserRole userRole = userRoleService.getRoleById(roleId);

        // Checks whether mapping already exists
        if (userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(userId, roleId)) {

            throw new RoleAlreadyAssignedException(userId, roleId); // Throws duplicate mapping exception
        }

        // Creates composite key object
        UserRoles.UserRolesId compositeId = new UserRoles.UserRolesId();

        // Sets user ID into composite key
        compositeId.setUserID(userId);

        // Sets role ID into composite key
        compositeId.setUserRoleID(roleId);

        // Creates UserRoles mapping object
        UserRoles mapping = new UserRoles();

        // Sets composite ID into mapping
        mapping.setId(compositeId);

        // Sets user object into mapping
        mapping.setUser(user);

        // Sets role object into mapping
        mapping.setUserRole(userRole);

        // Saves mapping into database
        return userRolesRepository.save(mapping);
    }

    // Removes role assignment from user
    @Override
    public boolean removeRoleFromUser(Integer userId, Integer roleId) {

        // Validates user existence
        userService.getUserById(userId);

        // Validates role existence
        userRoleService.getRoleById(roleId);

        // Creates composite key object
        UserRoles.UserRolesId compositeId = new UserRoles.UserRolesId();

        // Sets user ID into composite key
        compositeId.setUserID(userId);

        // Sets role ID into composite key
        compositeId.setUserRoleID(roleId);

        // Checks whether mapping exists
        if (!userRolesRepository.existsById(compositeId)) {

            throw new RoleNotFoundException(roleId); // Throws exception if mapping missing
        }

        // Deletes mapping from database
        userRolesRepository.deleteById(compositeId);

        // Returns true after successful deletion
        return true;
    }

    // Retrieves all roles assigned to a user
    @Override
    public List<UserRole> getRolesOfUser(Integer userId) {

        // Validates user existence
        userService.getUserById(userId);

        // Retrieves mappings, sorts them, extracts roles, and converts to list
        return userRolesRepository.findByUser_UserID(userId)
                .stream()
                .sorted()
                .map(UserRoles::getUserRole)
                .collect(Collectors.toList());
    }
}