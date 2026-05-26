package com.createtask.createtask.service;

import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.DuplicateUserException;
import com.createtask.createtask.exception.UserNotFoundException;
import com.createtask.createtask.exception.UserRoleMappingExistsException;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.repository.UserRolesRepository;
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

    // Creates a mock of UserRolesRepository — used for validating user-role mappings
    @Mock
    private UserRolesRepository userRolesRepository;

    // Creates UserServiceImpl and injects the mocked repositories automatically
    @InjectMocks
    private UserServiceImpl userService;

    // Reusable AppUser object shared across all tests
    private AppUser testUser;

    // Runs before every test — builds a fresh AppUser to avoid state leakage between tests
    @BeforeEach
    void setUp() {

        testUser = new AppUser(); // Creates new AppUser instance

        testUser.setUserID(1); // Sets primary key
        testUser.setUsername("John"); // Sets username
        testUser.setPassword("password123"); // Sets password
        testUser.setEmail("john.doe@email.com"); // Sets email
        testUser.setFullName("John Doe"); // Sets full name
    }

    // 1. createUser — positive: user saved successfully when validations pass
    @Test
    void createUser_Success() {

        when(userRepository.existsById(1))
                .thenReturn(false); // Simulates unique ID

        when(userRepository.existsByEmail("john.doe@email.com"))
                .thenReturn(false); // Simulates unique email

        when(userRepository.save(testUser))
                .thenReturn(testUser); // Simulates successful save

        AppUser result = userService.createUser(testUser); // Calls method under test

        assertThat(result.getUsername())
                .isEqualTo("John"); // Verifies username

        verify(userRepository)
                .save(testUser); // Confirms save executed
    }

    // 2. createUser — negative: throws DuplicateUserException when ID already exists
    @Test
    void createUser_DuplicateUserID_ThrowsDuplicateUserException() {

        when(userRepository.existsById(1))
                .thenReturn(true); // Simulates duplicate ID

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("userID");

        verify(userRepository, never())
                .save(any());
    }

    // 3. createUser — negative: throws DuplicateUserException when email already exists
    @Test
    void createUser_DuplicateEmail_ThrowsDuplicateUserException() {

        when(userRepository.existsById(1))
                .thenReturn(false);

        when(userRepository.existsByEmail("john.doe@email.com"))
                .thenReturn(true); // Simulates duplicate email

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("email");

        verify(userRepository, never())
                .save(any());
    }

    // 4. createUser — negative: throws IllegalArgumentException for invalid username
    @Test
    void createUser_InvalidUsername_ThrowsIllegalArgumentException() {

        testUser.setUsername("john123"); // Invalid username

        when(userRepository.existsById(1))
                .thenReturn(false);

        when(userRepository.existsByEmail("john.doe@email.com"))
                .thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username must contain only letters");

        verify(userRepository, never())
                .save(any());
    }

    // 5. createUser — negative: throws IllegalArgumentException for invalid full name
    @Test
    void createUser_InvalidFullName_ThrowsIllegalArgumentException() {

        testUser.setFullName("John123"); // Invalid full name

        when(userRepository.existsById(1))
                .thenReturn(false);

        when(userRepository.existsByEmail("john.doe@email.com"))
                .thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("FullName must contain only letters");

        verify(userRepository, never())
                .save(any());
    }

    // 6. getUserById — positive: returns user when ID exists
    @Test
    void getUserById_Success() {

        when(userRepository.findById(1))
                .thenReturn(Optional.of(testUser)); // Simulates user found

        AppUser result = userService.getUserById(1);

        assertThat(result.getUserID())
                .isEqualTo(1);

        assertThat(result.getFullName())
                .isEqualTo("John Doe");
    }

    // 7. getUserById — negative: throws UserNotFoundException when user missing
    @Test
    void getUserById_NotFound_ThrowsUserNotFoundException() {

        when(userRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("999");
    }

    // 8. getAllUsersSorted — positive: TreeSet sorts users using compareTo
    @Test
    void getAllUsersSorted_ReturnsSortedUsers() {

        AppUser user2 = new AppUser();

        user2.setUserID(2);
        user2.setUsername("Jane");
        user2.setPassword("pass456");
        user2.setEmail("jane@email.com");
        user2.setFullName("Jane Smith");

        when(userRepository.findAll())
                .thenReturn(List.of(user2, testUser));

        TreeSet<AppUser> result = userService.getAllUsersSorted();

        assertThat(result.first().getUserID())
                .isEqualTo(1);

        assertThat(result.last().getUserID())
                .isEqualTo(2);
    }

    // 9. updateUser — positive: updates user successfully
    @Test
    void updateUser_Success() {

        AppUser updatedUser = new AppUser();

        updatedUser.setUserID(1);
        updatedUser.setUsername("Michael");
        updatedUser.setPassword("newpass123");
        updatedUser.setEmail("john.doe@email.com");
        updatedUser.setFullName("Michael Scott");

        when(userRepository.findById(1))
                .thenReturn(Optional.of(testUser));

        when(userRepository.save(any(AppUser.class)))
                .thenReturn(updatedUser);

        AppUser result = userService.updateUser(1, updatedUser);

        assertThat(result.getUsername())
                .isEqualTo("Michael");

        assertThat(result.getFullName())
                .isEqualTo("Michael Scott");
    }

    // 10. updateUser — negative: throws IllegalArgumentException for invalid username
    @Test
    void updateUser_InvalidUsername_ThrowsIllegalArgumentException() {

        AppUser updatedUser = new AppUser();

        updatedUser.setUsername("john123"); // Invalid username
        updatedUser.setPassword("newpass123");
        updatedUser.setEmail("john.doe@email.com");
        updatedUser.setFullName("John Doe");

        when(userRepository.findById(1))
                .thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.updateUser(1, updatedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username must contain only letters");
    }

    // 11. deleteUser — positive: deletes user successfully
    @Test
    void deleteUser_Success_ReturnsTrue() {

        when(userRepository.existsById(1))
                .thenReturn(true);

        when(userRolesRepository.existsByUser_UserID(1))
                .thenReturn(false); // No mappings exist

        doNothing().when(userRepository)
                .deleteById(1);

        boolean result = userService.deleteUser(1);

        assertThat(result)
                .isTrue();

        verify(userRepository)
                .deleteById(1);
    }

    // 12. deleteUser — negative: throws UserRoleMappingExistsException when mappings exist
    @Test
    void deleteUser_UserRoleMappingExists_ThrowsUserRoleMappingExistsException() {

        when(userRepository.existsById(1))
                .thenReturn(true);

        when(userRolesRepository.existsByUser_UserID(1))
                .thenReturn(true); // Simulates mapping exists

        assertThatThrownBy(() -> userService.deleteUser(1))
                .isInstanceOf(UserRoleMappingExistsException.class)
                .hasMessageContaining("role mappings exist");

        verify(userRepository, never())
                .deleteById(any());
    }
}