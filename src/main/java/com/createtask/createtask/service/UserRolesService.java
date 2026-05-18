package com.createtask.createtask.service;

import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.entity.UserRoles;

import java.util.List;

/**
 * UserRolesService defines the contract for managing the many-to-many
 * relationship between User and UserRole through the UserRoles junction table.
 */
public interface UserRolesService {

    /** Assigns a role to a user. Validates both exist and mapping is not duplicate. */
    UserRoles assignRoleToUser(Integer userId, Integer roleId);

    /**
     * Removes a role from a user.
     * Returns true after successful removal to confirm the operation completed.
     * Throws RoleNotFoundException if the mapping does not exist.
     */
    boolean removeRoleFromUser(Integer userId, Integer roleId);

    /** Returns all roles assigned to a user, sorted by UserRoles compareTo(). */
    List<UserRole> getRolesOfUser(Integer userId);
}