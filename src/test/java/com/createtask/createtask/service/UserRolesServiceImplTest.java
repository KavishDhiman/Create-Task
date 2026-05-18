package com.createtask.createtask.service;

import com.createtask.createtask.entity.User;
import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.entity.UserRoles;
import com.createtask.createtask.exception.RoleAlreadyAssignedException;
import com.createtask.createtask.exception.RoleNotFoundException;
import com.createtask.createtask.repository.UserRolesRepository;
import com.createtask.createtask.service.impl.UserRolesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserRolesServiceImpl.
 * Mocks UserRolesRepository, UserService, and UserRoleService
 * so all three dependencies are isolated from real DB and other service logic.
 * Tests cover role assignment, removal, and retrieval scenarios.
 */
@ExtendWith(MockitoExtension.class)
class UserRolesServiceImplTest {

    @Mock
    private UserRolesRepository userRolesRepository;

    /** Mocked so getUserById() can be stubbed without hitting the DB. */
    @Mock
    private UserService userService;

    /** Mocked so getRoleById() can be stubbed without hitting the DB. */
    @Mock
    private UserRoleService userRoleService;

    @InjectMocks
    private UserRolesServiceImpl userRolesService;

    private User testUser;
    private UserRole testRole;
    private UserRoles.UserRolesId compositeId;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserID(1);
        testUser.setUsername("john_doe");
        testUser.setPassword("password123");
        testUser.setEmail("john.doe@email.com");
        testUser.setFullName("John Doe");

        testRole = new UserRole();
        testRole.setUserRoleID(2);
        testRole.setRoleName("User");

        compositeId = new UserRoles.UserRolesId();
        compositeId.setUserID(1);
        compositeId.setUserRoleID(2);
    }

    // -------------------------------------------------------
    // assignRoleToUser — positive
    // -------------------------------------------------------

    @Test
    void assignRoleToUser_Success() {
        when(userService.getUserById(1)).thenReturn(testUser);
        when(userRoleService.getRoleById(2)).thenReturn(testRole);
        when(userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(1, 2)).thenReturn(false);

        UserRoles mapping = new UserRoles();
        mapping.setId(compositeId);
        mapping.setUser(testUser);
        mapping.setUserRole(testRole);

        when(userRolesRepository.save(any(UserRoles.class))).thenReturn(mapping);

        UserRoles result = userRolesService.assignRoleToUser(1, 2);

        assertThat(result.getUser().getUserID()).isEqualTo(1);
        assertThat(result.getUserRole().getUserRoleID()).isEqualTo(2);
        verify(userRolesRepository).save(any(UserRoles.class));
    }

    // -------------------------------------------------------
    // assignRoleToUser — negative: role already assigned
    // -------------------------------------------------------

    @Test
    void assignRoleToUser_AlreadyAssigned_ThrowsRoleAlreadyAssignedException() {
        when(userService.getUserById(1)).thenReturn(testUser);
        when(userRoleService.getRoleById(2)).thenReturn(testRole);
        when(userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(1, 2)).thenReturn(true);

        assertThatThrownBy(() -> userRolesService.assignRoleToUser(1, 2))
                .isInstanceOf(RoleAlreadyAssignedException.class)
                .hasMessageContaining("1")
                .hasMessageContaining("2");

        verify(userRolesRepository, never()).save(any());
    }

    // -------------------------------------------------------
    // assignRoleToUser — negative: user does not exist
    // -------------------------------------------------------

    @Test
    void assignRoleToUser_UserNotFound_ThrowsFromUserService() {
        when(userService.getUserById(999))
                .thenThrow(new com.createtask.createtask.exception.UserNotFoundException(999));

        assertThatThrownBy(() -> userRolesService.assignRoleToUser(999, 2))
                .isInstanceOf(com.createtask.createtask.exception.UserNotFoundException.class)
                .hasMessageContaining("999");
    }

    // -------------------------------------------------------
    // assignRoleToUser — negative: role does not exist
    // -------------------------------------------------------

    @Test
    void assignRoleToUser_RoleNotFound_ThrowsFromUserRoleService() {
        when(userService.getUserById(1)).thenReturn(testUser);
        when(userRoleService.getRoleById(999))
                .thenThrow(new RoleNotFoundException(999));

        assertThatThrownBy(() -> userRolesService.assignRoleToUser(1, 999))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("999");
    }

    // -------------------------------------------------------
    // removeRoleFromUser — positive
    // -------------------------------------------------------

    @Test
    void removeRoleFromUser_Success_ReturnsTrue() {
        when(userService.getUserById(1)).thenReturn(testUser);
        when(userRoleService.getRoleById(2)).thenReturn(testRole);
        when(userRolesRepository.existsById(any(UserRoles.UserRolesId.class))).thenReturn(true);
        doNothing().when(userRolesRepository).deleteById(any(UserRoles.UserRolesId.class));

        boolean result = userRolesService.removeRoleFromUser(1, 2);

        assertThat(result).isTrue();
        verify(userRolesRepository).deleteById(any(UserRoles.UserRolesId.class));
    }

    // -------------------------------------------------------
    // removeRoleFromUser — negative: mapping does not exist
    // -------------------------------------------------------

    @Test
    void removeRoleFromUser_MappingNotFound_ThrowsRoleNotFoundException() {
        when(userService.getUserById(1)).thenReturn(testUser);
        when(userRoleService.getRoleById(2)).thenReturn(testRole);
        when(userRolesRepository.existsById(any(UserRoles.UserRolesId.class))).thenReturn(false);

        assertThatThrownBy(() -> userRolesService.removeRoleFromUser(1, 2))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("2");

        verify(userRolesRepository, never()).deleteById(any());
    }

    // -------------------------------------------------------
    // getRolesOfUser — positive
    // -------------------------------------------------------

    @Test
    void getRolesOfUser_Success_ReturnsSortedRoles() {
        when(userService.getUserById(1)).thenReturn(testUser);

        UserRoles mapping = new UserRoles();
        mapping.setId(compositeId);
        mapping.setUser(testUser);
        mapping.setUserRole(testRole);

        when(userRolesRepository.findByUser_UserID(1)).thenReturn(List.of(mapping));

        List<UserRole> result = userRolesService.getRolesOfUser(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRoleName()).isEqualTo("User");
    }

    // -------------------------------------------------------
    // getRolesOfUser — positive: returns empty list if no roles assigned
    // -------------------------------------------------------

    @Test
    void getRolesOfUser_NoRolesAssigned_ReturnsEmptyList() {
        when(userService.getUserById(1)).thenReturn(testUser);
        when(userRolesRepository.findByUser_UserID(1)).thenReturn(List.of());

        List<UserRole> result = userRolesService.getRolesOfUser(1);

        assertThat(result).isEmpty();
    }
}