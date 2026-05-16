package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Notification;
import com.createtask.createtask.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserID(902);
        testUser.setUsername("notification_user");
        testUser.setPassword("notify123");
        testUser.setEmail("notification.user@email.com");
        testUser.setFullName("Notification User");
        userRepository.save(testUser);

        testNotification = new Notification();
        testNotification.setNotificationID(901);
        testNotification.setText("JUnit notification message");
        testNotification.setCreatedAt(LocalDateTime.now());
        testNotification.setUser(testUser);
        notificationRepository.save(testNotification);
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteById(901);
        userRepository.deleteById(902);
    }

    @Test
    void testSaveNotification_Success() {
        Optional<Notification> found = notificationRepository.findById(901);

        assertThat(found).isPresent();
        assertThat(found.get().getText()).isEqualTo("JUnit notification message");
        assertThat(found.get().getUser().getUserID()).isEqualTo(902);
    }

    @Test
    void testFindAllNotifications_ContainsSeededData() {
        List<Notification> notifications = notificationRepository.findAll();

        assertThat(notifications).isNotEmpty();
        assertThat(notifications.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void testFindByUser_UserIDOrderByCreatedAtDesc_Success() {
        List<Notification> notifications =
                notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(902);

        assertThat(notifications).isNotEmpty();
        assertThat(notifications.get(0).getUser().getUserID()).isEqualTo(902);
        assertThat(notifications.get(0).getText()).isEqualTo("JUnit notification message");
    }

    @Test
    void testUpdateNotification_Success() {
        Notification notification = notificationRepository.findById(901).get();
        notification.setText("Updated notification message");
        notificationRepository.save(notification);

        Notification updated = notificationRepository.findById(901).get();
        assertThat(updated.getText()).isEqualTo("Updated notification message");
    }

    @Test
    void testDeleteNotification_Success() {
        notificationRepository.deleteById(901);

        Optional<Notification> deleted = notificationRepository.findById(901);
        assertThat(deleted).isNotPresent();

        notificationRepository.save(testNotification);
    }

    @Test
    void testFindById_NotFound() {
        Optional<Notification> found = notificationRepository.findById(9999);

        assertThat(found).isNotPresent();
    }

    @Test
    void testFindByUser_UserIDOrderByCreatedAtDesc_NotFound() {
        List<Notification> notifications =
                notificationRepository.findByUser_UserIDOrderByCreatedAtDesc(9999);

        assertThat(notifications).isEmpty();
    }
}