package com.createtask.createtask.service;

import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.DuplicateUserException;
import com.createtask.createtask.exception.UserNotFoundException;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.impl.UserServiceImpl;
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
class UserServiceImplTest {

    // Creates a mock of UserRepository — no real DB connection made
    @Mock
    private UserRepository userRepository;

    // Creates UserServiceImpl and injects the mocked repository into it automatically
    @InjectMocks
    private UserServiceImpl userService;

    // Reusable AppUser object shared across all tests
    private AppUser testUser;

    // Runs before every test — builds a fresh AppUser to avoid state leakage between tests
    @BeforeEach
    void setUp() {
        testUser = new AppUser(); // Creates new AppUser instance
        testUser.setUserID(1); // Sets primary key
        testUser.setUsername("john_doe"); // Sets username
        testUser.setPassword("password123"); // Sets password
        testUser.setEmail("john.doe@email.com"); // Sets email
        testUser.setFullName("John Doe"); // Sets full name
    }

    // 1. createUser — positive: user saved successfully when ID and email are unique
    @Test
    void createUser_Success() {
        when(userRepository.existsById(1)).thenReturn(false); // Simulates ID not existing in DB
        when(userRepository.existsByEmail("john.doe@email.com")).thenReturn(false); // Simulates email not existing
        when(userRepository.save(testUser)).thenReturn(testUser); // Simulates successful DB save

        AppUser result = userService.createUser(testUser); // Calls method under test

        assertThat(result.getUsername()).isEqualTo("john_doe"); // Verifies correct user returned
        verify(userRepository).save(testUser); // Confirms save was actually called once
    }

    // 2. createUser — negative: throws DuplicateUserException when user ID already exists
    @Test
    void createUser_DuplicateUserID_ThrowsDuplicateUserException() {
        when(userRepository.existsById(1)).thenReturn(true); // Simulates ID already present in DB

        assertThatThrownBy(() -> userService.createUser(testUser)) // Expects exception on create
                .isInstanceOf(DuplicateUserException.class) // Verifies exception is correct type
                .hasMessageContaining("userID"); // Verifies message mentions the conflicting field

        verify(userRepository, never()).save(any()); // Confirms save was never called
    }

    // 3. createUser — negative: throws DuplicateUserException when email already exists
    @Test
    void createUser_DuplicateEmail_ThrowsDuplicateUserException() {
        when(userRepository.existsById(1)).thenReturn(false); // Simulates ID is unique
        when(userRepository.existsByEmail("john.doe@email.com")).thenReturn(true); // Simulates email conflict

        assertThatThrownBy(() -> userService.createUser(testUser)) // Expects exception on create
                .isInstanceOf(DuplicateUserException.class) // Verifies exception is correct type
                .hasMessageContaining("email"); // Verifies message mentions the conflicting field

        verify(userRepository, never()).save(any()); // Confirms save was blocked correctly
    }

    // 4. getUserById — positive: returns correct user when ID exists in DB
    @Test
    void getUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser)); // Simulates user found

        AppUser result = userService.getUserById(1); // Calls method under test

        assertThat(result.getUserID()).isEqualTo(1); // Verifies returned ID matches
        assertThat(result.getFullName()).isEqualTo("John Doe"); // Verifies returned name matches
    }

    // 5. getUserById — negative: throws UserNotFoundException when user is absent
    @Test
    void getUserById_NotFound_ThrowsUserNotFoundException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty()); // Simulates no user found

        assertThatThrownBy(() -> userService.getUserById(999)) // Expects exception on lookup
                .isInstanceOf(UserNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("999"); // Verifies message contains the missing ID
    }

    // 6. getAllUsers — positive: returns full list of users from repository
    @Test
    void getAllUsers_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser)); // Simulates DB returning one user

        List<AppUser> result = userService.getAllUsers(); // Calls method under test

        assertThat(result).hasSize(1); // Verifies list has one entry
        assertThat(result.get(0).getUsername()).isEqualTo("john_doe"); // Verifies correct user in list
    }

    // 7. getAllUsersSorted — positive: TreeSet sorts users by userID via AppUser compareTo
    @Test
    void getAllUsersSorted_ReturnsSortedByUserID() {
        AppUser user2 = new AppUser(); // Creates second user with a higher ID
        user2.setUserID(2); // Higher ID — should appear after testUser in sorted set
        user2.setUsername("jane_smith");
        user2.setPassword("pass456");
        user2.setEmail("jane.smith@email.com");
        user2.setFullName("Jane Smith");

        when(userRepository.findAll()).thenReturn(List.of(user2, testUser)); // Returns unsorted list

        TreeSet<AppUser> result = userService.getAllUsersSorted(); // TreeSet sorts via compareTo

        assertThat(result.first().getUserID()).isEqualTo(1); // Verifies smallest ID is first
        assertThat(result.last().getUserID()).isEqualTo(2); // Verifies largest ID is last
    }

    // 8. updateUser — positive: updates fields and saves when email is unchanged
    @Test
    void updateUser_SameEmail_Success() {
        AppUser updatedUser = new AppUser(); // Creates update request with same email
        updatedUser.setUserID(1); // Same ID as existing user
        updatedUser.setUsername("john_updated"); // Updated username
        updatedUser.setPassword("newpass123"); // Updated password
        updatedUser.setEmail("john.doe@email.com"); // Same email — skips duplicate email check
        updatedUser.setFullName("John Updated"); // Updated full name

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser)); // Simulates user found
        when(userRepository.save(any(AppUser.class))).thenReturn(updatedUser); // Simulates save success

        AppUser result = userService.updateUser(1, updatedUser); // Calls method under test

        assertThat(result.getUsername()).isEqualTo("john_updated"); // Verifies username was updated
        assertThat(result.getFullName()).isEqualTo("John Updated"); // Verifies full name was updated
    }

    // 9. updateUser — negative: throws UserNotFoundException when user ID is not in DB
    @Test
    void updateUser_UserNotFound_ThrowsUserNotFoundException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty()); // Simulates no user found

        assertThatThrownBy(() -> userService.updateUser(999, testUser)) // Expects exception
                .isInstanceOf(UserNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("999"); // Verifies message contains the missing ID
    }

    // 10. updateUser — negative: throws DuplicateUserException when changed email belongs to another user
    @Test
    void updateUser_DuplicateEmail_ThrowsDuplicateUserException() {
        AppUser updatedUser = new AppUser(); // Creates update request with a different email
        updatedUser.setUsername("john_doe"); // Same username
        updatedUser.setEmail("taken@email.com"); // Different email that already exists in DB

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser)); // Simulates existing user found
        when(userRepository.existsByEmail("taken@email.com")).thenReturn(true); // Simulates email conflict

        assertThatThrownBy(() -> userService.updateUser(1, updatedUser)) // Expects exception
                .isInstanceOf(DuplicateUserException.class) // Verifies correct exception type
                .hasMessageContaining("email"); // Verifies message identifies the conflicting field
    }

    // 11. deleteUser — positive: returns true confirming successful deletion
    @Test
    void deleteUser_Success_ReturnsTrue() {
        when(userRepository.existsById(1)).thenReturn(true); // Simulates user exists in DB
        doNothing().when(userRepository).deleteById(1); // Simulates delete with no side effects

        boolean result = userService.deleteUser(1); // Calls method under test

        assertThat(result).isTrue(); // Verifies true returned on successful deletion
        verify(userRepository).deleteById(1); // Confirms deleteById was called with correct ID
    }

    // 12. deleteUser — negative: throws UserNotFoundException and never deletes when ID absent
    @Test
    void deleteUser_NotFound_ThrowsUserNotFoundException() {
        when(userRepository.existsById(999)).thenReturn(false); // Simulates user not found in DB

        assertThatThrownBy(() -> userService.deleteUser(999)) // Expects exception on delete
                .isInstanceOf(UserNotFoundException.class) // Verifies correct exception type
                .hasMessageContaining("999"); // Verifies message contains the missing ID

        verify(userRepository, never()).deleteById(any()); // Confirms deleteById was never called
    }
}