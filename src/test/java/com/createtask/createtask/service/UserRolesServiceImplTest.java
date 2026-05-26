package com.createtask.createtask.service;

import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.entity.UserRoles;
import com.createtask.createtask.exception.RoleAlreadyAssignedException;
import com.createtask.createtask.exception.RoleNotFoundException;
import com.createtask.createtask.exception.UserNotFoundException;
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

// Enables Mockito annotations — initializes @Mock and @InjectMocks before each test
@ExtendWith(MockitoExtension.class)
class UserRolesServiceImplTest {

    // Creates a mock of UserRolesRepository — no real DB connection made
    @Mock
    private UserRolesRepository userRolesRepository;

    // Creates a mock of UserService — stubs getUserById without hitting DB
    @Mock
    private UserService userService;

    // Creates a mock of UserRoleService — stubs getRoleById without hitting DB
    @Mock
    private UserRoleService userRoleService;

    // Creates UserRolesServiceImpl and injects all three mocked dependencies automatically
    @InjectMocks
    private UserRolesServiceImpl userRolesService;

    // Reusable test objects shared across all tests
    private AppUser testUser;
    private UserRole testRole;
    private UserRoles.UserRolesId compositeId;

    // Runs before every test — initializes fresh test data to avoid state leakage
    @BeforeEach
    void setUp() {
        testUser = new AppUser(); // Creates AppUser instance for testing
        testUser.setUserID(1); // Sets primary key
        testUser.setUsername("john_doe"); // Sets username
        testUser.setPassword("password123"); // Sets password
        testUser.setEmail("john.doe@email.com"); // Sets email
        testUser.setFullName("John Doe"); // Sets full name

        testRole = new UserRole(); // Creates UserRole instance for testing
        testRole.setUserRoleID(2); // Sets role primary key
        testRole.setRoleName("User"); // Sets role name

        compositeId = new UserRoles.UserRolesId(); // Creates composite key object
        compositeId.setUserID(1); // Sets user part of composite key
        compositeId.setUserRoleID(2); // Sets role part of composite key
    }

    // 1. assignRoleToUser — positive: mapping created and saved successfully
    @Test
    void assignRoleToUser_Success() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(2)).thenReturn(testRole); // Simulates role found
        when(userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(1, 2)).thenReturn(false); // Simulates mapping not existing

        UserRoles mapping = new UserRoles(); // Creates expected mapping result
        mapping.setId(compositeId); // Sets composite key on mapping
        mapping.setUser(testUser); // Sets user on mapping
        mapping.setUserRole(testRole); // Sets role on mapping

        when(userRolesRepository.save(any(UserRoles.class))).thenReturn(mapping); // Simulates successful save

        UserRoles result = userRolesService.assignRoleToUser(1, 2); // Calls method under test

        assertThat(result.getUser().getUserID()).isEqualTo(1); // Verifies correct user ID on result
        assertThat(result.getUserRole().getUserRoleID()).isEqualTo(2); // Verifies correct role ID on result
        verify(userRolesRepository).save(any(UserRoles.class)); // Confirms save was actually called
    }

    // 2. assignRoleToUser — negative: throws RoleAlreadyAssignedException when mapping exists
    @Test
    void assignRoleToUser_AlreadyAssigned_ThrowsRoleAlreadyAssignedException() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(2)).thenReturn(testRole); // Simulates role found
        when(userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(1, 2)).thenReturn(true); // Simulates mapping already exists

        assertThatThrownBy(() -> userRolesService.assignRoleToUser(1, 2)) // Expects exception on assign
                .isInstanceOf(RoleAlreadyAssignedException.class) // Verifies correct exception type
                .hasMessageContaining("1") // Verifies message contains user ID
                .hasMessageContaining("2"); // Verifies message contains role ID

        verify(userRolesRepository, never()).save(any()); // Confirms save was never called
    }

    // 3. assignRoleToUser — negative: throws UserNotFoundException when user is absent
    @Test
    void assignRoleToUser_UserNotFound_ThrowsUserNotFoundException() {
        when(userService.getUserById(999)).thenThrow(new UserNotFoundException(999)); // Simulates user not found

        assertThatThrownBy(() -> userRolesService.assignRoleToUser(999, 2)) // Expects exception on assign
                .isInstanceOf(UserNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("999"); // Verifies message contains missing user ID
    }

    // 4. assignRoleToUser — negative: throws RoleNotFoundException when role is absent
    @Test
    void assignRoleToUser_RoleNotFound_ThrowsRoleNotFoundException() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(999)).thenThrow(new RoleNotFoundException(999)); // Simulates role not found

        assertThatThrownBy(() -> userRolesService.assignRoleToUser(1, 999)) // Expects exception on assign
                .isInstanceOf(RoleNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("999"); // Verifies message contains missing role ID
    }

    // 5. removeRoleFromUser — positive: returns true after successful deletion
    @Test
    void removeRoleFromUser_Success_ReturnsTrue() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(2)).thenReturn(testRole); // Simulates role found
        when(userRolesRepository.existsById(any(UserRoles.UserRolesId.class))).thenReturn(true); // Simulates mapping exists
        doNothing().when(userRolesRepository).deleteById(any(UserRoles.UserRolesId.class)); // Simulates delete with no side effects

        boolean result = userRolesService.removeRoleFromUser(1, 2); // Calls method under test

        assertThat(result).isTrue(); // Verifies true returned on successful deletion
        verify(userRolesRepository).deleteById(any(UserRoles.UserRolesId.class)); // Confirms deleteById was called
    }

    // 6. removeRoleFromUser — negative: throws RuntimeException when mapping does not exist
    @Test
    void removeRoleFromUser_MappingNotFound_ThrowsRuntimeException() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(2)).thenReturn(testRole); // Simulates role found
        when(userRolesRepository.existsById(any(UserRoles.UserRolesId.class))).thenReturn(false); // Simulates mapping absent

        assertThatThrownBy(() -> userRolesService.removeRoleFromUser(1, 2)) // Expects exception on remove
                .isInstanceOf(RuntimeException.class) // Verifies RuntimeException — actual impl throws RuntimeException not RoleNotFoundException
                .hasMessageContaining("Role mapping does not exist"); // Verifies message matches actual impl message
    }

    // 7. removeRoleFromUser — negative: deleteById never called when mapping is missing
    @Test
    void removeRoleFromUser_MappingNotFound_DeleteNeverCalled() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(2)).thenReturn(testRole); // Simulates role found
        when(userRolesRepository.existsById(any(UserRoles.UserRolesId.class))).thenReturn(false); // Simulates mapping absent

        assertThatThrownBy(() -> userRolesService.removeRoleFromUser(1, 2)) // Expects exception
                .isInstanceOf(RuntimeException.class); // Verifies RuntimeException thrown

        verify(userRolesRepository, times(0)).deleteById(any()); // Confirms deleteById was never invoked
    }

    // 8. getRolesOfUser — positive: returns sorted list of roles for a user
    @Test
    void getRolesOfUser_Success_ReturnsSortedRoles() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found

        UserRoles mapping = new UserRoles(); // Creates mapping object for test
        mapping.setId(compositeId); // Sets composite key on mapping
        mapping.setUser(testUser); // Sets user on mapping
        mapping.setUserRole(testRole); // Sets role on mapping

        when(userRolesRepository.findByUser_UserID(1)).thenReturn(List.of(mapping)); // Simulates one mapping returned

        List<UserRole> result = userRolesService.getRolesOfUser(1); // Calls method under test

        assertThat(result).hasSize(1); // Verifies exactly one role returned
        assertThat(result.get(0).getRoleName()).isEqualTo("User"); // Verifies correct role name
    }

    // 9. getRolesOfUser — positive: returns empty list when no roles are assigned to user
    @Test
    void getRolesOfUser_NoRolesAssigned_ReturnsEmptyList() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRolesRepository.findByUser_UserID(1)).thenReturn(List.of()); // Simulates no mappings found

        List<UserRole> result = userRolesService.getRolesOfUser(1); // Calls method under test

        assertThat(result).isEmpty(); // Verifies empty list returned
    }

    // 10. getRolesOfUser — negative: throws UserNotFoundException when user does not exist
    @Test
    void getRolesOfUser_UserNotFound_ThrowsUserNotFoundException() {
        when(userService.getUserById(999)).thenThrow(new UserNotFoundException(999)); // Simulates user not found

        assertThatThrownBy(() -> userRolesService.getRolesOfUser(999)) // Expects exception on lookup
                .isInstanceOf(UserNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("999"); // Verifies message contains missing user ID
    }

    // 11. assignRoleToUser — positive: save is called exactly once on successful assignment
    @Test
    void assignRoleToUser_SaveCalledExactlyOnce() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(2)).thenReturn(testRole); // Simulates role found
        when(userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(1, 2)).thenReturn(false); // Simulates no existing mapping

        UserRoles mapping = new UserRoles(); // Creates mapping result object
        mapping.setId(compositeId); // Sets composite key
        mapping.setUser(testUser); // Sets user
        mapping.setUserRole(testRole); // Sets role

        when(userRolesRepository.save(any(UserRoles.class))).thenReturn(mapping); // Simulates save

        userRolesService.assignRoleToUser(1, 2); // Calls method under test

        verify(userRolesRepository, times(1)).save(any(UserRoles.class)); // Confirms save called exactly once
    }

    // 12. removeRoleFromUser — positive: deleteById called exactly once on successful removal
    @Test
    void removeRoleFromUser_Success_DeleteCalledExactlyOnce() {
        when(userService.getUserById(1)).thenReturn(testUser); // Simulates user found
        when(userRoleService.getRoleById(2)).thenReturn(testRole); // Simulates role found
        when(userRolesRepository.existsById(any(UserRoles.UserRolesId.class))).thenReturn(true); // Simulates mapping exists
        doNothing().when(userRolesRepository).deleteById(any(UserRoles.UserRolesId.class)); // Simulates delete

        userRolesService.removeRoleFromUser(1, 2); // Calls method under test

        verify(userRolesRepository, times(1)).deleteById(any(UserRoles.UserRolesId.class)); // Confirms deleteById called exactly once
    }
}