package com.createtask.createtask.service;

import com.createtask.createtask.entity.UserRole;

import java.util.List;
import java.util.TreeSet;

// Service interface for role-related business operations
public interface UserRoleService {

    // Creates a new role
    UserRole createRole(UserRole userRole);

    // Retrieves all roles as a list
    List<UserRole> getAllRoles();

    // Retrieves all roles sorted using TreeSet
    TreeSet<UserRole> getAllRolesSorted();

    // Retrieves role by role ID
    UserRole getRoleById(Integer roleId);
}