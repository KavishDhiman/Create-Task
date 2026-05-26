package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.NotificationRequestDTO;
import com.createtask.createtask.dto.response.NotificationResponseDTO;
import com.createtask.createtask.entity.Notification;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.NotificationAlreadyExistsException;
import com.createtask.createtask.exception.NotificationNotFoundException;
import com.createtask.createtask.exception.NotificationRecipientNotFoundException;
import com.createtask.createtask.repository.NotificationRepository;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Tells JUnit 5 to use Mockito to initialise @Mock and @InjectMocks fields
class NotificationServiceImplTest {

    @Mock // Mockito creates a mock — no real DB calls are made
    private NotificationRepository notificationRepository;

    @Mock // Mockito creates a mock — no real DB calls are made
    private UserRepository userRepository;

    @InjectMocks // Creates a real NotificationServiceImpl and injects the two mocks above into it
    private NotificationServiceImpl notificationService;

    // ─── Shared test fixtures ────────────────────────────────────────────────

    private AppUser testUser;              // Reusable User entity across test methods
    private Notification testNotification; // Reusable Notification entity across test methods
    private NotificationRequestDTO requestDTO; // Reusable request DTO for create tests

    @BeforeEach // Runs before every single test — resets state so tests remain independent
    void setUp() {
        // Build a fake User entity to simulate a valid recipient
        testUser = new AppUser();
        testUser.setUserID(1);             // Assign a known ID for use in assertions
        testUser.setFullName("Test User"); // Used in response DTO mapping

        // Build a fake Notification entity as if it were retrieved from or saved to the DB
        testNotification = new Notification();
        testNotification.setNotificationID(101);                           // Known ID for assertions
        testNotification.setText("Test notification message");             // Message content to assert on
        testNotification.setCreatedAt(LocalDateTime.of(2024, 6, 1, 10, 0)); // Fixed timestamp for predictable assertions
        testNotification.setUser(testUser);                                // Link notification to the fake user

        // Build a request DTO simulating what the controller sends to the service.
        // notificationID is set to 101 — must match testNotification so the
        // existsById(101) duplicate check can be properly stubbed in each test.
        requestDTO = new NotificationRequestDTO(); // Instantiate FIRST before setting any fields
        requestDTO.setNotificationID(101);         // Must be set — service calls existsById(requestDTO.getNotificationID())
        requestDTO.setUserId(1);                   // Matches testUser's ID
        requestDTO.setText("Test notification message"); // Matches testNotification's text
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CREATE TESTS  (3 tests — matches 3 branches in createNotification)
    //─────────────────────────────────────────────────────────────────────────

    @Test // Negative: notification ID already exists → duplicate exception thrown before any user lookup
    void createNotification_DuplicateId_ThrowsException() {
        // Arrange — existsById returns true, meaning a notification with this ID already exists
        when(notificationRepository.existsById(101)).thenReturn(true); // Duplicate detected

        // Act & Assert — service must throw NotificationAlreadyExistsException immediately
        assertThatThrownBy(() -> notificationService.createNotification(requestDTO))
                .isInstanceOf(NotificationAlreadyExistsException.class) // Must be correct exception type
                .hasMessageContaining("101"); // Message must reference the duplicate ID

        // Verify — user lookup and save must never be reached when a duplicate is detected
        verify(userRepository, never()).findById(anyInt()); // User lookup must NOT happen
        verify(notificationRepository, never()).save(any(Notification.class)); // Save must NOT happen
    }

    @Test // Negative: notification ID is new but user does not exist → recipient exception thrown
    void createNotification_UserNotFound_ThrowsException() {
        // Arrange — no duplicate, but user lookup fails
        when(notificationRepository.existsById(101)).thenReturn(false); // No duplicate — ID is new
        when(userRepository.findById(1)).thenReturn(Optional.empty());  // User does not exist

        // Act & Assert — service must throw NotificationRecipientNotFoundException
        assertThatThrownBy(() -> notificationService.createNotification(requestDTO))
                .isInstanceOf(NotificationRecipientNotFoundException.class) // Must be correct exception type
                .hasMessageContaining("1"); // Message must reference the missing user ID

        // Verify — save must never be called if the recipient user doesn't exist
        verify(notificationRepository, never()).save(any(Notification.class)); // Save must NOT happen
    }

    @Test // Positive: no duplicate ID + valid user → notification saved → populated DTO returned
    void createNotification_Success() {
        // Arrange — no duplicate, user found, save returns the test notification
        when(notificationRepository.existsById(101)).thenReturn(false);         // No duplicate
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));     // User found
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification); // Save succeeds

        // Act — call the method under test
        NotificationResponseDTO result = notificationService.createNotification(requestDTO);

        // Assert — returned DTO must contain values from the saved entity
        assertThat(result).isNotNull();                                          // Result must not be null
        assertThat(result.getNotificationID()).isEqualTo(101);                   // ID from saved entity
        assertThat(result.getText()).isEqualTo("Test notification message");     // Text from saved entity
        assertThat(result.getUserId()).isEqualTo(1);                             // User ID from saved entity
        assertThat(result.getUserName()).isEqualTo("Test User");                 // User name from saved entity

        // Verify — all three steps must have been called exactly once
        verify(notificationRepository, times(1)).existsById(101); // Duplicate check happened
        verify(userRepository, times(1)).findById(1);             // User lookup happened
        verify(notificationRepository, times(1)).save(any(Notification.class)); // Save happened
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET BY ID TESTS  (2 tests — matches 2 branches in getNotificationById)
    // ─────────────────────────────────────────────────────────────────────────

    @Test // Positive: notification exists → correct DTO returned with all mapped fields
    void getNotificationById_Success() {
        // Arrange — simulate finding the notification by ID
        when(notificationRepository.findById(101)).thenReturn(Optional.of(testNotification)); // Found

        // Act
        NotificationResponseDTO result = notificationService.getNotificationById(101);

        // Assert — all fields must be correctly mapped from the entity
        assertThat(result).isNotNull();                                      // Result must not be null
        assertThat(result.getNotificationID()).isEqualTo(101);               // Must match the queried ID
        assertThat(result.getText()).isEqualTo("Test notification message"); // Must match the message text
        assertThat(result.getUserId()).isEqualTo(1);                         // Must map user ID correctly
        assertThat(result.getUserName()).isEqualTo("Test User");             // Must map user name correctly
    }

    @Test // Negative: notification does not exist → NotificationNotFoundException thrown
    void getNotificationById_NotFound_ThrowsException() {
        // Arrange — simulate notification not found
        when(notificationRepository.findById(999)).thenReturn(Optional.empty()); // Not found

        // Act & Assert
        assertThatThrownBy(() -> notificationService.getNotificationById(999))
                .isInstanceOf(NotificationNotFoundException.class) // Correct exception type
                .hasMessageContaining("999");                       // Message must reference the missing ID
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET BY USER ID TESTS  (3 tests — matches 3 branches in getNotificationsByUserId)
    // ─────────────────────────────────────────────────────────────────────────

    @Test // Negative: user does not exist → exception thrown, notification repo never queried
    void getNotificationsByUserId_UserNotFound_ThrowsException() {
        // Arrange — user does not exist
        when(userRepository.existsById(99)).thenReturn(false); // User not found

        // Act & Assert
        assertThatThrownBy(() -> notificationService.getNotificationsByUserId(99))
                .isInstanceOf(NotificationRecipientNotFoundException.class) // Correct exception type
                .hasMessageContaining("99");                                 // Message must reference missing user ID

        // Verify — notification repo must never be queried when user doesn't exist
        verify(notificationRepository, never()).findByUser_UserIDOrderByCreatedAtDesc(anyInt());
    }

    @Test // Positive: valid user with no notifications → empty list returned (not null)
    void getNotificationsByUserId_EmptyList_Success() {
        // Arrange — user exists but has no notifications yet
        when(userRepository.existsById(1)).thenReturn(true);                              // User exists
        when(notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(1))
                .thenReturn(Collections.emptyList());                                     // No notifications

        // Act
        List<NotificationResponseDTO> results = notificationService.getNotificationsByUserId(1);

        // Assert — must return an empty list, never null
        assertThat(results).isNotNull(); // Must return a list, not null
        assertThat(results).isEmpty();   // No notifications for this user
    }

    @Test // Positive: valid user with multiple notifications → list returned ordered newest first
    void getNotificationsByUserId_Success() {
        // Arrange — build a second newer notification for the same user
        Notification secondNotification = new Notification();
        secondNotification.setNotificationID(102);                              // Different ID
        secondNotification.setText("Second notification");                      // Different text
        secondNotification.setCreatedAt(LocalDateTime.of(2024, 6, 2, 10, 0)); // Newer timestamp
        secondNotification.setUser(testUser);                                   // Same user

        when(userRepository.existsById(1)).thenReturn(true); // User exists
        when(notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(1))
                .thenReturn(Arrays.asList(secondNotification, testNotification)); // Newest first

        // Act
        List<NotificationResponseDTO> results = notificationService.getNotificationsByUserId(1);

        // Assert — list must be ordered newest first with correct IDs
        assertThat(results).isNotNull();                                   // List must not be null
        assertThat(results).hasSize(2);                                    // Must contain both notifications
        assertThat(results.get(0).getNotificationID()).isEqualTo(102);     // Newest first
        assertThat(results.get(1).getNotificationID()).isEqualTo(101);     // Oldest second
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE TESTS  (2 tests — matches 2 branches in deleteNotification)
    // ─────────────────────────────────────────────────────────────────────────

    @Test // Positive: notification exists → deleted and confirmation message returned
    void deleteNotification_Success() {
        // Arrange — notification exists and deleteById completes without error
        when(notificationRepository.existsById(101)).thenReturn(true); // Notification exists
        doNothing().when(notificationRepository).deleteById(101);      // deleteById is void — stub with doNothing

        // Act
        String result = notificationService.deleteNotification(101);

        // Assert — returned message must confirm deletion and reference the ID
        assertThat(result).isNotNull();                          // Must return a message
        assertThat(result).contains("101");                      // Message must reference deleted notification ID
        assertThat(result).containsIgnoringCase("deleted");      // Message must confirm deletion

        // Verify — both existsById and deleteById must be called exactly once
        verify(notificationRepository, times(1)).existsById(101); // Existence check happened
        verify(notificationRepository, times(1)).deleteById(101); // Delete happened
    }

    @Test // Negative: notification does not exist → exception thrown, deleteById never called
    void deleteNotification_NotFound_ThrowsException() {
        // Arrange — notification does not exist
        when(notificationRepository.existsById(999)).thenReturn(false); // Notification not found

        // Act & Assert
        assertThatThrownBy(() -> notificationService.deleteNotification(999))
                .isInstanceOf(NotificationNotFoundException.class) // Correct exception type
                .hasMessageContaining("999");                       // Message must reference the missing ID

        // Verify — deleteById must never be called if the notification doesn't exist
        verify(notificationRepository, never()).deleteById(anyInt()); // Delete must NOT happen
    }
}