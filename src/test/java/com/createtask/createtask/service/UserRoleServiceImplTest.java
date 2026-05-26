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

// Enables Mockito annotations — initializes @Mock and @InjectMocks before each test
@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    // Creates a mock of UserRoleRepository — no real DB connection made
    @Mock
    private UserRoleRepository userRoleRepository;

    // Creates UserRoleServiceImpl and injects the mocked repository automatically
    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    // Reusable UserRole object shared across all tests
    private UserRole testRole;

    // Runs before every test — builds a fresh UserRole to avoid state leakage
    @BeforeEach
    void setUp() {
        testRole = new UserRole(); // Creates new UserRole instance
        testRole.setUserRoleID(1); // Sets primary key
        testRole.setRoleName("Admin"); // Sets valid role name — letters only
    }

    // 1. createRole — positive: role saved successfully when ID and name are unique and name is valid
    @Test
    void createRole_Success() {
        when(userRoleRepository.existsById(1)).thenReturn(false); // Simulates ID not existing in DB
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(false); // Simulates role name not existing
        when(userRoleRepository.save(testRole)).thenReturn(testRole); // Simulates successful DB save

        UserRole result = userRoleService.createRole(testRole); // Calls method under test

        assertThat(result.getRoleName()).isEqualTo("Admin"); // Verifies correct role returned
        verify(userRoleRepository).save(testRole); // Confirms save was actually called once
    }

    // 2. createRole — negative: throws DuplicateRoleException when role ID already exists
    @Test
    void createRole_DuplicateRoleID_ThrowsDuplicateRoleException() {
        when(userRoleRepository.existsById(1)).thenReturn(true); // Simulates ID already present in DB

        assertThatThrownBy(() -> userRoleService.createRole(testRole)) // Expects exception on create
                .isInstanceOf(DuplicateRoleException.class) // Verifies correct exception type
                .hasMessageContaining("Role ID already exists"); // Verifies message matches actual impl message

        verify(userRoleRepository, never()).save(any()); // Confirms save was never called
    }

    // 3. createRole — negative: throws DuplicateRoleException when role name already exists
    @Test
    void createRole_DuplicateRoleName_ThrowsDuplicateRoleException() {
        when(userRoleRepository.existsById(1)).thenReturn(false); // Simulates ID is unique
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(true); // Simulates name conflict in DB

        assertThatThrownBy(() -> userRoleService.createRole(testRole)) // Expects exception on create
                .isInstanceOf(DuplicateRoleException.class) // Verifies correct exception type
                .hasMessageContaining("Admin"); // Verifies message contains the conflicting name

        verify(userRoleRepository, never()).save(any()); // Confirms save was blocked
    }

    // 4. createRole — negative: throws IllegalArgumentException when role name contains numbers
    @Test
    void createRole_InvalidRoleName_WithNumbers_ThrowsIllegalArgumentException() {
        UserRole invalidRole = new UserRole(); // Creates role with invalid name
        invalidRole.setUserRoleID(5); // Sets unique ID
        invalidRole.setRoleName("Admin123"); // Invalid — contains numbers, fails regex check

        when(userRoleRepository.existsById(5)).thenReturn(false); // Simulates ID is unique
        when(userRoleRepository.existsByRoleName("Admin123")).thenReturn(false); // Simulates name is unique

        assertThatThrownBy(() -> userRoleService.createRole(invalidRole)) // Expects exception on create
                .isInstanceOf(IllegalArgumentException.class) // Verifies correct exception type
                .hasMessageContaining("Role name must contain only letters"); // Verifies message matches impl

        verify(userRoleRepository, never()).save(any()); // Confirms save was never called
    }

    // 5. createRole — negative: throws IllegalArgumentException when role name contains special characters
    @Test
    void createRole_InvalidRoleName_WithSpecialChars_ThrowsIllegalArgumentException() {
        UserRole invalidRole = new UserRole(); // Creates role with special character in name
        invalidRole.setUserRoleID(6); // Sets unique ID
        invalidRole.setRoleName("Admin@Role"); // Invalid — contains @, fails regex

        when(userRoleRepository.existsById(6)).thenReturn(false); // Simulates ID is unique
        when(userRoleRepository.existsByRoleName("Admin@Role")).thenReturn(false); // Simulates name is unique

        assertThatThrownBy(() -> userRoleService.createRole(invalidRole)) // Expects exception on create
                .isInstanceOf(IllegalArgumentException.class) // Verifies correct exception type
                .hasMessageContaining("Role name must contain only letters"); // Verifies message matches impl
    }

    // 6. getAllRoles — positive: returns all roles from repository as a plain list
    @Test
    void getAllRoles_ReturnsAllRoles() {
        when(userRoleRepository.findAll()).thenReturn(List.of(testRole)); // Simulates DB returning one role

        List<UserRole> result = userRoleService.getAllRoles(); // Calls method under test

        assertThat(result).hasSize(1); // Verifies list has one entry
        assertThat(result.get(0).getRoleName()).isEqualTo("Admin"); // Verifies correct role in list
    }

    // 7. getAllRoles — positive: returns empty list when no roles exist
    @Test
    void getAllRoles_Empty_ReturnsEmptyList() {
        when(userRoleRepository.findAll()).thenReturn(List.of()); // Simulates DB returning no roles

        List<UserRole> result = userRoleService.getAllRoles(); // Calls method under test

        assertThat(result).isEmpty(); // Verifies empty list returned
    }

    // 8. getAllRolesSorted — positive: TreeSet sorts by userRoleID ascending using custom comparator
    @Test
    void getAllRolesSorted_ReturnsSortedByRoleID() {
        UserRole role3 = new UserRole(); // Creates role with higher ID
        role3.setUserRoleID(3); // ID 3 — should appear after testRole (ID 1)
        role3.setRoleName("Manager"); // Sets role name

        UserRole role2 = new UserRole(); // Creates role with middle ID
        role2.setUserRoleID(2); // ID 2 — should appear between testRole and role3
        role2.setRoleName("User"); // Sets role name

        when(userRoleRepository.findAll()).thenReturn(List.of(role3, testRole, role2)); // Returns unsorted list

        TreeSet<UserRole> result = userRoleService.getAllRolesSorted(); // TreeSet sorts by ID via comparator

        assertThat(result.first().getUserRoleID()).isEqualTo(1); // Verifies lowest ID is first
        assertThat(result.last().getUserRoleID()).isEqualTo(3); // Verifies highest ID is last
    }

    // 9. getRoleById — positive: returns correct role when ID exists in DB
    @Test
    void getRoleById_Success() {
        when(userRoleRepository.findById(1)).thenReturn(Optional.of(testRole)); // Simulates role found

        UserRole result = userRoleService.getRoleById(1); // Calls method under test

        assertThat(result.getUserRoleID()).isEqualTo(1); // Verifies returned ID matches
        assertThat(result.getRoleName()).isEqualTo("Admin"); // Verifies returned name matches
    }

    // 10. getRoleById — negative: throws RoleNotFoundException when ID not found in DB
    @Test
    void getRoleById_NotFound_ThrowsRoleNotFoundException() {
        when(userRoleRepository.findById(999)).thenReturn(Optional.empty()); // Simulates no role found

        assertThatThrownBy(() -> userRoleService.getRoleById(999)) // Expects exception on lookup
                .isInstanceOf(RoleNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("999"); // Verifies message contains the missing ID
    }

    // 11. getRoleById — negative: exception message always contains the missing ID
    @Test
    void getRoleById_NotFound_MessageContainsMissingId() {
        when(userRoleRepository.findById(55)).thenReturn(Optional.empty()); // Simulates no role found for ID 55

        assertThatThrownBy(() -> userRoleService.getRoleById(55)) // Expects exception on lookup
                .isInstanceOf(RoleNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("55"); // Verifies message contains ID 55
    }

    // 12. createRole — positive: existsById called exactly once before saving
    @Test
    void createRole_ExistsByIdCalledOnce() {
        when(userRoleRepository.existsById(1)).thenReturn(false); // Simulates ID is unique
        when(userRoleRepository.existsByRoleName("Admin")).thenReturn(false); // Simulates name is unique
        when(userRoleRepository.save(testRole)).thenReturn(testRole); // Simulates save

        userRoleService.createRole(testRole); // Calls method under test

        verify(userRoleRepository, times(1)).existsById(1); // Confirms existsById called exactly once
    }
}