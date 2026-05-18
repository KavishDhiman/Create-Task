package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.NotificationRequestDTO; // Request DTO used to create notifications
import com.createtask.createtask.dto.response.NotificationResponseDTO; // Response DTO returned by service methods
import com.createtask.createtask.entity.Notification; // Notification entity used in mocked repository calls
import com.createtask.createtask.entity.AppUser; // User entity needed to simulate a valid recipient
import com.createtask.createtask.exception.NotificationNotFoundException; // Expected in negative delete/get tests
import com.createtask.createtask.exception.NotificationRecipientNotFoundException; // Expected when user ID is invalid
import com.createtask.createtask.repository.NotificationRepository; // Mocked — no real DB calls in unit tests
import com.createtask.createtask.repository.UserRepository; // Mocked — no real DB calls in unit tests
import com.createtask.createtask.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach; // Runs setup before each test method
import org.junit.jupiter.api.Test; // Marks a method as a JUnit 5 test case
import org.junit.jupiter.api.extension.ExtendWith; // Registers Mockito extension with JUnit 5
import org.mockito.InjectMocks; // Creates the class under test and injects mocks into it
import org.mockito.Mock; // Creates a Mockito mock for the annotated field
import org.mockito.junit.jupiter.MockitoExtension; // Mockito extension that initialises mocks automatically

import java.time.LocalDateTime; // Used to set createdAt on test notification entities
import java.util.Arrays; // Used to build multi-item lists for list test cases
import java.util.Collections; // Used to build empty list for negative list test case
import java.util.List; // Return type for list-based service methods
import java.util.Optional; // Used to simulate findById returning present or empty

import static org.assertj.core.api.Assertions.assertThat; // Fluent assertion library — cleaner than JUnit assertions
import static org.assertj.core.api.Assertions.assertThatThrownBy; // Fluent way to assert exception throwing
import static org.mockito.ArgumentMatchers.any; // Matches any argument of a given type in verify/when
import static org.mockito.Mockito.*; // Imports when(), verify(), never(), times(), etc.

@ExtendWith(MockitoExtension.class) // Tells JUnit 5 to use Mockito to initialise @Mock and @InjectMocks fields
class NotificationServiceImplTest {

    @Mock // Mockito creates a mock — no real DB calls are made
    private NotificationRepository notificationRepository;

    @Mock // Mockito creates a mock — no real DB calls are made
    private UserRepository userRepository;

    @InjectMocks // Creates a real NotificationServiceImpl and injects the two mocks above into it
    private NotificationServiceImpl notificationService;

    // ─── Shared test fixtures ────────────────────────────────────────────────

    private AppUser testUser; // Reusable User entity across test methods
    private Notification testNotification; // Reusable Notification entity across test methods
    private NotificationRequestDTO requestDTO; // Reusable request DTO for create tests

    @BeforeEach // Runs before every single test — resets state so tests remain independent
    void setUp() {
        // Build a fake User entity to simulate a valid recipient
        testUser = new AppUser();
        testUser.setUserID(1); // Assign a known ID for use in assertions
        testUser.setFullName("Test User"); // Used in toResponseDTO() mapping

        // Build a fake Notification entity as if it were retrieved from the DB
        testNotification = new Notification();
        testNotification.setNotificationID(101); // Known ID for assertions
        testNotification.setText("Test notification message"); // Message content to assert on
        testNotification.setCreatedAt(LocalDateTime.of(2024, 6, 1, 10, 0)); // Fixed timestamp for predictable assertions
        testNotification.setUser(testUser); // Link the notification to the fake user

        // Build a request DTO simulating what the controller sends to the service
        requestDTO = new NotificationRequestDTO();
        requestDTO.setUserId(1); // Matches testUser's ID
        requestDTO.setText("Test notification message"); // Same text as testNotification for consistency
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CREATE TESTS
    // ─────────────────────────────────────────────────────────────────────────

    @Test // Positive: creating a notification when user exists should return a populated DTO
    void createNotification_Success() {
        // Arrange — define what the mocks return when called
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser)); // User lookup succeeds
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification); // Save returns testNotification

        // Act — call the method under test
        NotificationResponseDTO result = notificationService.createNotification(requestDTO);

        // Assert — verify the returned DTO contains the expected values
        assertThat(result).isNotNull(); // Result must not be null
        assertThat(result.getNotificationID()).isEqualTo(101); // Must match testNotification's ID
        assertThat(result.getText()).isEqualTo("Test notification message"); // Must match message text
        assertThat(result.getUserId()).isEqualTo(1); // Must match testUser's ID
        assertThat(result.getUserName()).isEqualTo("Test User"); // Must match testUser's full name

        // Verify — ensure the correct repository methods were actually called
        verify(userRepository, times(1)).findById(1); // User lookup must happen exactly once
        verify(notificationRepository, times(1)).save(any(Notification.class)); // Save must happen exactly once
    }

    @Test // Negative: creating a notification when user does not exist should throw exception
    void createNotification_UserNotFound_ThrowsException() {
        // Arrange — simulate user not found
        when(userRepository.findById(99)).thenReturn(Optional.empty()); // Returns empty — user doesn't exist

        requestDTO.setUserId(99); // Set an ID that doesn't match any user

        // Act & Assert — expect NotificationRecipientNotFoundException to be thrown
        assertThatThrownBy(() -> notificationService.createNotification(requestDTO))
                .isInstanceOf(NotificationRecipientNotFoundException.class) // Must be correct exception type
                .hasMessageContaining("99"); // Exception message must mention the missing user ID

        // Verify — save must never be called if the user doesn't exist
        verify(notificationRepository, never()).save(any(Notification.class)); // Save must NOT be invoked
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET BY ID TESTS
    // ─────────────────────────────────────────────────────────────────────────

    @Test // Positive: fetching a notification that exists should return the correct DTO
    void getNotificationById_Success() {
        // Arrange — simulate finding the notification by ID
        when(notificationRepository.findById(101)).thenReturn(Optional.of(testNotification)); // Found

        // Act
        NotificationResponseDTO result = notificationService.getNotificationById(101);

        // Assert
        assertThat(result).isNotNull(); // Result must not be null
        assertThat(result.getNotificationID()).isEqualTo(101); // Must match the queried ID
        assertThat(result.getText()).isEqualTo("Test notification message"); // Must match the message text
        assertThat(result.getUserId()).isEqualTo(1); // Must map user ID correctly
    }

    @Test // Negative: fetching a notification that does not exist should throw exception
    void getNotificationById_NotFound_ThrowsException() {
        // Arrange — simulate notification not found
        when(notificationRepository.findById(999)).thenReturn(Optional.empty()); // Not found

        // Act & Assert
        assertThatThrownBy(() -> notificationService.getNotificationById(999))
                .isInstanceOf(NotificationNotFoundException.class) // Correct exception type
                .hasMessageContaining("999"); // Message must reference the missing ID
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET BY USER ID TESTS
    // ─────────────────────────────────────────────────────────────────────────

    @Test // Positive: fetching notifications for a valid user with existing notifications
    void getNotificationsByUserId_Success() {
        // Arrange — second notification for the same user to test list size
        Notification secondNotification = new Notification();
        secondNotification.setNotificationID(102); // Different ID
        secondNotification.setText("Second notification"); // Different text
        secondNotification.setCreatedAt(LocalDateTime.of(2024, 6, 2, 10, 0)); // Newer timestamp
        secondNotification.setUser(testUser); // Same user

        when(userRepository.existsById(1)).thenReturn(true); // User exists
        when(notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(1))
                .thenReturn(Arrays.asList(secondNotification, testNotification)); // Returns newest first

        // Act
        List<NotificationResponseDTO> results = notificationService.getNotificationsByUserId(1);

        // Assert
        assertThat(results).isNotNull(); // List must not be null
        assertThat(results).hasSize(2); // Must contain both notifications
        assertThat(results.get(0).getNotificationID()).isEqualTo(102); // Newest first
        assertThat(results.get(1).getNotificationID()).isEqualTo(101); // Oldest second
    }

    @Test // Positive: fetching notifications for a valid user who has no notifications returns empty list
    void getNotificationsByUserId_EmptyList_Success() {
        // Arrange
        when(userRepository.existsById(1)).thenReturn(true); // User exists
        when(notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(1))
                .thenReturn(Collections.emptyList()); // User has no notifications

        // Act
        List<NotificationResponseDTO> results = notificationService.getNotificationsByUserId(1);

        // Assert
        assertThat(results).isNotNull(); // Must return a list, not null
        assertThat(results).isEmpty(); // No notifications for this user
    }

    @Test // Negative: fetching notifications for a non-existent user should throw exception
    void getNotificationsByUserId_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.existsById(99)).thenReturn(false); // User does not exist

        // Act & Assert
        assertThatThrownBy(() -> notificationService.getNotificationsByUserId(99))
                .isInstanceOf(NotificationRecipientNotFoundException.class) // Correct exception
                .hasMessageContaining("99"); // Message must reference the missing user ID

        // Verify — repository must never be called for notifications if user doesn't exist
        verify(notificationRepository, never()).findByUser_UserIDOrderByCreatedAtDesc(anyInt());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE TESTS
    // ─────────────────────────────────────────────────────────────────────────

    @Test // Positive: deleting an existing notification should return confirmation message
    void deleteNotification_Success() {
        // Arrange
        when(notificationRepository.existsById(101)).thenReturn(true); // Notification exists
        doNothing().when(notificationRepository).deleteById(101); // deleteById has no return value — stub with doNothing

        // Act
        String result = notificationService.deleteNotification(101);

        // Assert
        assertThat(result).isNotNull(); // Must return a message
        assertThat(result).contains("101"); // Message must reference the deleted notification ID
        assertThat(result).containsIgnoringCase("deleted"); // Message must confirm deletion

        // Verify — both existsById and deleteById must be called exactly once
        verify(notificationRepository, times(1)).existsById(101);
        verify(notificationRepository, times(1)).deleteById(101);
    }

    @Test // Negative: deleting a notification that does not exist should throw exception
    void deleteNotification_NotFound_ThrowsException() {
        // Arrange
        when(notificationRepository.existsById(999)).thenReturn(false); // Notification does not exist

        // Act & Assert
        assertThatThrownBy(() -> notificationService.deleteNotification(999))
                .isInstanceOf(NotificationNotFoundException.class) // Correct exception type
                .hasMessageContaining("999"); // Message must reference the missing ID

        // Verify — deleteById must never be called if notification doesn't exist
        verify(notificationRepository, never()).deleteById(anyInt()); // Delete must NOT be invoked
    }
}