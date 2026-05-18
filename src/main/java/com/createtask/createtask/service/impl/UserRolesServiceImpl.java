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

/**
 * UserRolesServiceImpl manages the many-to-many relationship between
 * User and UserRole through the UserRoles junction table.
 * Delegates user and role existence validation to their respective services.
 */
@Service
public class UserRolesServiceImpl implements UserRolesService {

    private final UserRolesRepository userRolesRepository;

    /** Used to validate user existence and fetch the User entity for mapping. */
    private final UserService userService;

    /** Used to validate role existence and fetch the UserRole entity for mapping. */
    private final UserRoleService userRoleService;

    public UserRolesServiceImpl(UserRolesRepository userRolesRepository,
                                UserService userService,
                                UserRoleService userRoleService) {
        this.userRolesRepository = userRolesRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    /**
     * Validates user exists, role exists, and mapping is not a duplicate.
     * Constructs a composite key from userId and roleId before saving.
     */
    @Override
    public UserRoles assignRoleToUser(Integer userId, Integer roleId) {
        AppUser user = userService.getUserById(userId);
        UserRole userRole = userRoleService.getRoleById(roleId);

        if (userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(userId, roleId)) {
            throw new RoleAlreadyAssignedException(userId, roleId);
        }

        UserRoles.UserRolesId compositeId = new UserRoles.UserRolesId();
        compositeId.setUserID(userId);
        compositeId.setUserRoleID(roleId);

        UserRoles mapping = new UserRoles();
        mapping.setId(compositeId);
        mapping.setUser(user);
        mapping.setUserRole(userRole);

        return userRolesRepository.save(mapping);
    }

    /**
     * Validates user, role, and existing mapping before deletion.
     * Returns true after successful removal to confirm the operation completed.
     * Throws RoleNotFoundException if the mapping does not currently exist.
     */
    @Override
    public boolean removeRoleFromUser(Integer userId, Integer roleId) {
        userService.getUserById(userId);
        userRoleService.getRoleById(roleId);

        UserRoles.UserRolesId compositeId = new UserRoles.UserRolesId();
        compositeId.setUserID(userId);
        compositeId.setUserRoleID(roleId);

        if (!userRolesRepository.existsById(compositeId)) {
            throw new RoleNotFoundException(roleId);
        }

        userRolesRepository.deleteById(compositeId);
        return true;
    }

    /**
     * Retrieves all role mappings for the user, sorts them using
     * UserRoles compareTo() via stream.sorted(), then extracts
     * only the UserRole from each mapping using a method reference.
     */
    @Override
    public List<UserRole> getRolesOfUser(Integer userId) {
        userService.getUserById(userId);

        return userRolesRepository.findByUser_UserID(userId)
                .stream()
                .sorted()
                .map(UserRoles::getUserRole)
                .collect(Collectors.toList());
    }
}