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

/**
 * Unit tests for UserServiceImpl.
 * Uses Mockito to mock UserRepository so no real DB connection is needed.
 * Each test is isolated and tests only the service business logic.
 *
 * @ExtendWith(MockitoExtension.class) initializes mocks automatically before each test.
 * @Mock creates a mock instance of UserRepository.
 * @InjectMocks creates UserServiceImpl and injects the mocked repository into it.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /** Reusable test User object set up before each test. */
    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setUserID(1);
        testUser.setUsername("john_doe");
        testUser.setPassword("password123");
        testUser.setEmail("john.doe@email.com");
        testUser.setFullName("John Doe");
    }

    // createUser — positive

    @Test
    void createUser_Success() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john.doe@email.com")).thenReturn(false);
        when(userRepository.save(testUser)).thenReturn(testUser);

        AppUser result = userService.createUser(testUser);

        assertThat(result.getUsername()).isEqualTo("john_doe");
        verify(userRepository).save(testUser);
    }

    // createUser — negative: duplicate username

    @Test
    void createUser_DuplicateUsername_ThrowsDuplicateUserException() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("username");

        verify(userRepository, never()).save(any());
    }

    // createUser — negative: duplicate email

    @Test
    void createUser_DuplicateEmail_ThrowsDuplicateUserException() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john.doe@email.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("email");

        verify(userRepository, never()).save(any());
    }

    // getUserById — positive

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        AppUser result = userService.getUserById(1);

        assertThat(result.getUserID()).isEqualTo(1);
        assertThat(result.getFullName()).isEqualTo("John Doe");
    }

    // getUserById — negative: user not found

    @Test
    void getUserById_NotFound_ThrowsUserNotFoundException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("999");
    }

    // getAllUsers — positive

    @Test
    void getAllUsers_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<AppUser> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("john_doe");
    }

    // getAllUsersSorted — positive: TreeSet uses compareTo

    @Test
    void getAllUsersSorted_ReturnsSortedByUserID() {
        AppUser user2 = new AppUser();
        user2.setUserID(2);
        user2.setUsername("jane_smith");
        user2.setPassword("pass456");
        user2.setEmail("jane.smith@email.com");
        user2.setFullName("Jane Smith");

        when(userRepository.findAll()).thenReturn(List.of(user2, testUser));

        TreeSet<AppUser> result = userService.getAllUsersSorted();

        assertThat(result.first().getUserID()).isEqualTo(1);
        assertThat(result.last().getUserID()).isEqualTo(2);
    }

    // updateUser — positive

    @Test
    void updateUser_Success() {
        AppUser updatedUser = new AppUser();
        updatedUser.setUserID(1);
        updatedUser.setUsername("john_updated");
        updatedUser.setPassword("newpass123");
        updatedUser.setEmail("john.updated@email.com");
        updatedUser.setFullName("John Updated");

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("john_updated")).thenReturn(false);
        when(userRepository.existsByEmail("john.updated@email.com")).thenReturn(false);
        when(userRepository.save(any(AppUser.class))).thenReturn(updatedUser);

        AppUser result = userService.updateUser(1, updatedUser);

        assertThat(result.getUsername()).isEqualTo("john_updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@email.com");
    }

    // updateUser — negative: user not found

    @Test
    void updateUser_UserNotFound_ThrowsUserNotFoundException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(999, testUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("999");
    }

    // updateUser — negative: new username already taken by another user

    @Test
    void updateUser_DuplicateUsername_ThrowsDuplicateUserException() {
        AppUser updatedUser = new AppUser();
        updatedUser.setUsername("jane_smith");
        updatedUser.setEmail("john.doe@email.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("jane_smith")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1, updatedUser))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("username");
    }

    // deleteUser — positive

    @Test
    void deleteUser_Success_ReturnsTrue() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        boolean result = userService.deleteUser(1);

        assertThat(result).isTrue();
        verify(userRepository).deleteById(1);
    }

    // deleteUser — negative: user not found

    @Test
    void deleteUser_NotFound_ThrowsUserNotFoundException() {
        when(userRepository.existsById(999)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(999))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("999");

        verify(userRepository, never()).deleteById(any());
    }
}