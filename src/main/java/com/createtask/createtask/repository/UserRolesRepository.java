package com.createtask.createtask.repository;

import com.createtask.createtask.entity.UserRoles;
import com.createtask.createtask.entity.UserRoles.UserRolesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository interface for UserRoles mapping table operations
@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, UserRolesId> { // Extends JpaRepository for CRUD operations

    // Retrieves all role mappings for a specific user
    List<UserRoles> findByUser_UserID(int userID);

    // Checks whether a specific user-role mapping already exists
    boolean existsByUser_UserIDAndUserRole_UserRoleID(int userID, int userRoleID);

    boolean existsByUser_UserID(int userID);
}