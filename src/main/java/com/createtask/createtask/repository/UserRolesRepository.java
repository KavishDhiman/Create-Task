package com.createtask.createtask.repository;

import com.createtask.createtask.entity.UserRoles;
import com.createtask.createtask.entity.UserRoles.UserRolesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, UserRolesId> {

    List<UserRoles> findByUser_UserID(int userID);

    List<UserRoles> findByUserRole_UserRoleID(int userRoleID);

    boolean existsByUser_UserIDAndUserRole_UserRoleID(int userID, int userRoleID);
}