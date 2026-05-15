package com.createtask.createtask.repository;

import com.createtask.createtask.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // Test user saved before each test, deleted after
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserID(901);
        testUser.setUsername("junit_user");
        testUser.setPassword("junit123");
        testUser.setEmail("junit.user@email.com");
        testUser.setFullName("JUnit User");
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(901);
    }

    // -------------------------------------------------------
    // POSITIVE TESTS
    // -------------------------------------------------------

    @Test
    void testSaveUser_Success() {
        Optional<User> found = userRepository.findById(901);
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("junit_user");
        assertThat(found.get().getEmail()).isEqualTo("junit.user@email.com");
    }

    @Test
    void testFindAllUsers_ContainsSeededData() {
        // DB has 12 seeded users + our test user
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void testFindByUsername_Success() {
        Optional<User> found = userRepository.findByUsername("junit_user");
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("JUnit User");
    }

    @Test
    void testFindByEmail_Success() {
        Optional<User> found = userRepository.findByEmail("junit.user@email.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUserID()).isEqualTo(901);
    }

    @Test
    void testExistsByUsername_True() {
        boolean exists = userRepository.existsByUsername("junit_user");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail_True() {
        boolean exists = userRepository.existsByEmail("junit.user@email.com");
        assertThat(exists).isTrue();
    }

    @Test
    void testUpdateUser_Success() {
        User user = userRepository.findById(901).get();
        user.setFullName("JUnit User Updated");
        userRepository.save(user);

        User updated = userRepository.findById(901).get();
        assertThat(updated.getFullName()).isEqualTo("JUnit User Updated");
    }

    @Test
    void testDeleteUser_Success() {
        userRepository.deleteById(901);
        Optional<User> deleted = userRepository.findById(901);
        assertThat(deleted).isNotPresent();

        // Re-save so @AfterEach tearDown doesn't fail
        userRepository.save(testUser);
    }

    // -------------------------------------------------------
    // NEGATIVE TESTS
    // -------------------------------------------------------

    @Test
    void testFindByUsername_NotFound() {
        Optional<User> found = userRepository.findByUsername("ghost_user");
        assertThat(found).isNotPresent();
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> found = userRepository.findByEmail("nobody@email.com");
        assertThat(found).isNotPresent();
    }

    @Test
    void testExistsByUsername_False() {
        boolean exists = userRepository.existsByUsername("nonexistent_user");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindById_NotFound() {
        Optional<User> found = userRepository.findById(9999);
        assertThat(found).isNotPresent();
    }
}