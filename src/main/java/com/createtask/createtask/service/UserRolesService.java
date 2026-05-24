package com.createtask.createtask.service;

import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.entity.UserRoles;

import java.util.List;

// Service interface for managing user-role mappings
public interface UserRolesService {

    // Assigns a role to a user
    UserRoles assignRoleToUser(Integer userId, Integer roleId);

    // Removes a role from a user
    boolean removeRoleFromUser(Integer userId, Integer roleId);

    // Retrieves all roles assigned to a user
    List<UserRole> getRolesOfUser(Integer userId);
}