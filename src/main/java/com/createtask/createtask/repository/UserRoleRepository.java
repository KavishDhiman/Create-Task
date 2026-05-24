package com.createtask.createtask.repository;

import com.createtask.createtask.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for UserRole database operations
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> { // Extends JpaRepository for CRUD operations

    // Checks whether role name already exists
    boolean existsByRoleName(String roleName);
}