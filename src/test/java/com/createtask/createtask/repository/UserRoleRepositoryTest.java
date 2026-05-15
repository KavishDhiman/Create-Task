package com.createtask.createtask.repository;

import com.createtask.createtask.entity.UserRole;
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
class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    private UserRole testRole;

    @BeforeEach
    void setUp() {
        testRole = new UserRole();
        testRole.setUserRoleID(901);
        testRole.setRoleName("JUnit Tester");
        userRoleRepository.save(testRole);
    }

    @AfterEach
    void tearDown() {
        userRoleRepository.deleteById(901);
    }

    // -------------------------------------------------------
    // POSITIVE TESTS
    // -------------------------------------------------------

    @Test
    void testSaveRole_Success() {
        Optional<UserRole> found = userRoleRepository.findById(901);
        assertThat(found).isPresent();
        assertThat(found.get().getRoleName()).isEqualTo("JUnit Tester");
    }

    @Test
    void testFindAllRoles_ContainsSeededData() {
        List<UserRole> roles = userRoleRepository.findAll();
        assertThat(roles).isNotEmpty();
        assertThat(roles.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void testFindByRoleName_Success() {
        Optional<UserRole> found = userRoleRepository.findByRoleName("JUnit Tester");
        assertThat(found).isPresent();
        assertThat(found.get().getUserRoleID()).isEqualTo(901);
    }

    @Test
    void testExistsByRoleName_True() {
        boolean exists = userRoleRepository.existsByRoleName("JUnit Tester");
        assertThat(exists).isTrue();
    }

    @Test
    void testUpdateRole_Success() {
        UserRole role = userRoleRepository.findById(901).get();
        role.setRoleName("JUnit Tester Updated");
        userRoleRepository.save(role);

        UserRole updated = userRoleRepository.findById(901).get();
        assertThat(updated.getRoleName()).isEqualTo("JUnit Tester Updated");
    }

    @Test
    void testDeleteRole_Success() {
        userRoleRepository.deleteById(901);
        Optional<UserRole> deleted = userRoleRepository.findById(901);
        assertThat(deleted).isNotPresent();

        // Re-save so @AfterEach tearDown doesn't fail
        userRoleRepository.save(testRole);
    }

    // -------------------------------------------------------
    // NEGATIVE TESTS
    // -------------------------------------------------------

    @Test
    void testFindByRoleName_NotFound() {
        Optional<UserRole> found = userRoleRepository.findByRoleName("Ghost Role");
        assertThat(found).isNotPresent();
    }

    @Test
    void testExistsByRoleName_False() {
        boolean exists = userRoleRepository.existsByRoleName("Nonexistent Role");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindById_NotFound() {
        Optional<UserRole> found = userRoleRepository.findById(9999);
        assertThat(found).isNotPresent();
    }
}