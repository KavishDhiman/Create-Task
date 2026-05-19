package com.createtask.createtask.service;

import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.exception.DuplicateRoleException;
import com.createtask.createtask.exception.RoleNotFoundException;
import com.createtask.createtask.repository.UserRoleRepository;
import com.createtask.createtask.service.impl.UserRoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    private UserRole testRole;

    @BeforeEach
    void setUp() {
        testRole = new UserRole();
        testRole.setUserRoleID(1);
        testRole.setRoleName("Admin");
    }

    // 1. createRole — positive: role saved successfully
    @Test
    void createRole_Success() {
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(false);
        when(userRoleRepository.save(testRole)).thenReturn(testRole);

        UserRole result = userRoleService.createRole(testRole);

        assertThat(result.getRoleName()).isEqualTo("Admin");
        verify(userRoleRepository).save(testRole);
    }

    // 2. createRole — positive: existsByRoleName called exactly once
    @Test
    void createRole_ExistsByRoleNameCalledOnce() {
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(false);
        when(userRoleRepository.save(testRole)).thenReturn(testRole);

        userRoleService.createRole(testRole);

        verify(userRoleRepository, times(1)).existsByRoleName("Admin");
    }

    // 3. createRole — negative: duplicate role name throws exception
    @Test
    void createRole_DuplicateRoleName_ThrowsDuplicateRoleException() {
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(true);

        assertThatThrownBy(() -> userRoleService.createRole(testRole))
                .isInstanceOf(DuplicateRoleException.class)
                .hasMessageContaining("Admin");

        verify(userRoleRepository, never()).save(any());
    }

    // 4. getAllRoles — positive: returns all roles
    @Test
    void getAllRoles_ReturnsAllRoles() {
        when(userRoleRepository.findAll()).thenReturn(List.of(testRole));

        List<UserRole> result = userRoleService.getAllRoles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRoleName()).isEqualTo("Admin");
    }

    // 5. getAllRoles — positive: returns empty list when no roles exist
    @Test
    void getAllRoles_Empty_ReturnsEmptyList() {
        when(userRoleRepository.findAll()).thenReturn(List.of());

        List<UserRole> result = userRoleService.getAllRoles();

        assertThat(result).isEmpty();
    }

    // 6. getAllRolesSorted — positive: sorted by userRoleID via compareTo
    @Test
    void getAllRolesSorted_ReturnsSortedByRoleId() {

        UserRole roleUser = new UserRole();
        roleUser.setUserRoleID(2);
        roleUser.setRoleName("User");

        UserRole roleManager = new UserRole();
        roleManager.setUserRoleID(3);
        roleManager.setRoleName("Manager");

        when(userRoleRepository.findAll())
                .thenReturn(List.of(roleUser, testRole, roleManager));

        TreeSet<UserRole> result = userRoleService.getAllRolesSorted();
        assertThat(result.first().getRoleName()).isEqualTo("Admin");
        assertThat(result.last().getRoleName()).isEqualTo("Manager");
    }

    // 7. getAllRolesSorted — positive: single role returns correctly in TreeSet
    @Test
    void getAllRolesSorted_SingleRole_ReturnsSingleElement() {
        when(userRoleRepository.findAll()).thenReturn(List.of(testRole));

        TreeSet<UserRole> result = userRoleService.getAllRolesSorted();

        assertThat(result).hasSize(1);
        assertThat(result.first().getRoleName()).isEqualTo("Admin");
    }

    // 8. getRoleById — positive: returns correct role
    @Test
    void getRoleById_Success() {
        when(userRoleRepository.findById(1)).thenReturn(Optional.of(testRole));

        UserRole result = userRoleService.getRoleById(1);

        assertThat(result.getUserRoleID()).isEqualTo(1);
        assertThat(result.getRoleName()).isEqualTo("Admin");
    }

    // 9. getRoleById — negative: role not found throws exception
    @Test
    void getRoleById_NotFound_ThrowsRoleNotFoundException() {
        when(userRoleRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userRoleService.getRoleById(999))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("999");
    }

    // 10. getRoleById — negative: exception message contains the missing ID
    @Test
    void getRoleById_NotFound_ExceptionMessageContainsMissingId() {
        when(userRoleRepository.findById(55)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userRoleService.getRoleById(55))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("55");
    }
}