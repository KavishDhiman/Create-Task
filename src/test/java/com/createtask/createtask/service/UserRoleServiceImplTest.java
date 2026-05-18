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

/**
 * Unit tests for UserRoleServiceImpl.
 * Mocks UserRoleRepository so no real DB connection is required.
 * Tests cover role creation, retrieval, sorting, and not-found scenarios.
 */
@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    /** Reusable test UserRole object set up before each test. */
    private UserRole testRole;

    @BeforeEach
    void setUp() {
        testRole = new UserRole();
        testRole.setUserRoleID(1);
        testRole.setRoleName("Admin");
    }

    // createRole — positive

    @Test
    void createRole_Success() {
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(false);
        when(userRoleRepository.save(testRole)).thenReturn(testRole);

        UserRole result = userRoleService.createRole(testRole);

        assertThat(result.getRoleName()).isEqualTo("Admin");
        verify(userRoleRepository).save(testRole);
    }

    // createRole — negative: duplicate role name

    @Test
    void createRole_DuplicateRoleName_ThrowsDuplicateRoleException() {
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(true);

        assertThatThrownBy(() -> userRoleService.createRole(testRole))
                .isInstanceOf(DuplicateRoleException.class)
                .hasMessageContaining("Admin");

        verify(userRoleRepository, never()).save(any());
    }

    // getAllRoles — positive

    @Test
    void getAllRoles_ReturnsAllRoles() {
        when(userRoleRepository.findAll()).thenReturn(List.of(testRole));

        List<UserRole> result = userRoleService.getAllRoles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRoleName()).isEqualTo("Admin");
    }

    // getAllRolesSorted — positive: TreeSet uses compareTo (sorted by role ID)

    @Test
    void getAllRolesSorted_ReturnsSortedByRoleId() {

        UserRole roleB = new UserRole();
        roleB.setUserRoleID(2);
        roleB.setRoleName("User");

        UserRole roleA = new UserRole();
        roleA.setUserRoleID(3);
        roleA.setRoleName("Manager");

        when(userRoleRepository.findAll()).thenReturn(List.of(roleB, testRole, roleA));

        TreeSet<UserRole> result = userRoleService.getAllRolesSorted();

        // Sorted by userRoleID:

        assertThat(result.first().getRoleName()).isEqualTo("Admin");
        assertThat(result.last().getRoleName()).isEqualTo("Manager");
    }

    // getRoleById — positive

    @Test
    void getRoleById_Success() {
        when(userRoleRepository.findById(1)).thenReturn(Optional.of(testRole));

        UserRole result = userRoleService.getRoleById(1);

        assertThat(result.getUserRoleID()).isEqualTo(1);
        assertThat(result.getRoleName()).isEqualTo("Admin");
    }

    // getRoleById — negative: role not found

    @Test
    void getRoleById_NotFound_ThrowsRoleNotFoundException() {
        when(userRoleRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userRoleService.getRoleById(999))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("999");
    }
}