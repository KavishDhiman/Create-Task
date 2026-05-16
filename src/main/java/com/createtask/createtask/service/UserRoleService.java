package com.createtask.createtask.service;

import com.createtask.createtask.entity.UserRole;

import java.util.List;
import java.util.TreeSet;

/**
 * UserRoleService defines the contract for role-related business operations.
 * Implemented by UserRoleServiceImpl.
 */
public interface UserRoleService {

    /** Creates a new role after validating the role name is unique. */
    UserRole createRole(UserRole userRole);

    /** Returns all roles as an unordered list. */
    List<UserRole> getAllRoles();

    /**
     * Returns all roles sorted alphabetically by roleName using UserRole's compareTo().
     * TreeSet handles ordering automatically on insertion.
     */
    TreeSet<UserRole> getAllRolesSorted();

    /** Fetches a role by ID. Used internally for role assignment validation. */
    UserRole getRoleById(Integer roleId);
}