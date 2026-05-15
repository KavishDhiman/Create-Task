package com.createtask.createtask.repository;

import com.createtask.createtask.entity.User;
import com.createtask.createtask.entity.UserRole;
import com.createtask.createtask.entity.UserRoles;
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
class UserRolesRepositoryTest {

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    private User testUser;
    private UserRole testRole;
    private UserRoles.UserRolesId compositeId;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserID(902);
        testUser.setUsername("roles_test_user");
        testUser.setPassword("rolespass");
        testUser.setEmail("roles.test@email.com");
        testUser.setFullName("Roles Test User");
        userRepository.save(testUser);

        testRole = new UserRole();
        testRole.setUserRoleID(902);
        testRole.setRoleName("Roles Tester");
        userRoleRepository.save(testRole);

        compositeId = new UserRoles.UserRolesId();
        compositeId.setUserID(902);
        compositeId.setUserRoleID(902);

        UserRoles mapping = new UserRoles();
        mapping.setId(compositeId);
        mapping.setUser(testUser);
        mapping.setUserRole(testRole);
        userRolesRepository.save(mapping);
    }

    @AfterEach
    void tearDown() {
        userRolesRepository.deleteById(compositeId);
        userRoleRepository.deleteById(902);
        userRepository.deleteById(902);
    }

    @Test
    void testSaveUserRoles_Success() {
        Optional<UserRoles> found = userRolesRepository.findById(compositeId);
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUsername()).isEqualTo("roles_test_user");
        assertThat(found.get().getUserRole().getRoleName()).isEqualTo("Roles Tester");
    }

    @Test
    void testFindByUserID_Success() {
        List<UserRoles> mappings = userRolesRepository.findByUser_UserID(902);
        assertThat(mappings).isNotEmpty();
        assertThat(mappings.get(0).getUserRole().getRoleName()).isEqualTo("Roles Tester");
    }

    @Test
    void testFindByUserRoleID_Success() {
        List<UserRoles> mappings = userRolesRepository.findByUserRole_UserRoleID(902);
        assertThat(mappings).isNotEmpty();
        assertThat(mappings.get(0).getUser().getUsername()).isEqualTo("roles_test_user");
    }

    @Test
    void testExistsByUserIDAndRoleID_True() {
        boolean exists = userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(902, 902);
        assertThat(exists).isTrue();
    }

    @Test
    void testDeleteUserRoles_Success() {
        userRolesRepository.deleteById(compositeId);
        Optional<UserRoles> deleted = userRolesRepository.findById(compositeId);
        assertThat(deleted).isNotPresent();

        UserRoles mapping = new UserRoles();
        mapping.setId(compositeId);
        mapping.setUser(testUser);
        mapping.setUserRole(testRole);
        userRolesRepository.save(mapping);
    }

    @Test
    void testFindAll_ContainsMappings() {
        List<UserRoles> all = userRolesRepository.findAll();
        assertThat(all).isNotEmpty();
    }

    @Test
    void testFindByUserID_NotFound() {
        List<UserRoles> mappings = userRolesRepository.findByUser_UserID(9999);
        assertThat(mappings).isEmpty();
    }

    @Test
    void testFindByUserRoleID_NotFound() {
        List<UserRoles> mappings = userRolesRepository.findByUserRole_UserRoleID(9999);
        assertThat(mappings).isEmpty();
    }

    @Test
    void testExistsByUserIDAndRoleID_False() {
        boolean exists = userRolesRepository.existsByUser_UserIDAndUserRole_UserRoleID(9999, 9999);
        assertThat(exists).isFalse();
    }

    @Test
    void testFindById_NotFound() {
        UserRoles.UserRolesId fakeId = new UserRoles.UserRolesId();
        fakeId.setUserID(9999);
        fakeId.setUserRoleID(9999);

        Optional<UserRoles> found = userRolesRepository.findById(fakeId);
        assertThat(found).isNotPresent();
    }
}