package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private Project testProject;

    @BeforeEach
    void setUp() {
        User user = userRepository.findById(1).get();

        testProject = new Project();
        testProject.setProjectID(9001);
        testProject.setProjectName("Test Project");
        testProject.setDescription("Test Description");
        testProject.setStartDate(LocalDate.of(2024, 1, 1));
        testProject.setEndDate(LocalDate.of(2024, 6, 30));
        testProject.setUser(user);
        projectRepository.save(testProject);
    }

    @AfterEach
    void tearDown() {
        if (projectRepository.existsById(9001)) {
            projectRepository.deleteById(9001);
        }
    }


    @Test
    void testSaveProject_Success() {
        Optional<Project> found = projectRepository.findById(9001);
        assertThat(found).isPresent();
        assertThat(found.get().getProjectName()).isEqualTo("Test Project");
    }

    @Test
    void testFindAllProjects_NotEmpty() {
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).isNotEmpty();
    }

    @Test
    void testFindByUserID_Success() {
        List<Project> projects = projectRepository.findByUser_UserID(1);
        assertThat(projects).isNotEmpty();
    }

    @Test
    void testFindByProjectName_Success() {
        List<Project> projects = projectRepository
                .findByProjectNameContainingIgnoreCase("Test");
        assertThat(projects).isNotEmpty();
    }

    @Test
    void testUpdateProject_Success() {
        Project p = projectRepository.findById(9001).get();
        p.setProjectName("Updated Project");
        projectRepository.save(p);

        Project updated = projectRepository.findById(9001).get();
        assertThat(updated.getProjectName()).isEqualTo("Updated Project");
    }

    @Test
    void testDeleteProject_Success() {
        projectRepository.deleteById(9001);
        Optional<Project> deleted = projectRepository.findById(9001);
        assertThat(deleted).isNotPresent();

        // Re-save so @AfterEach tearDown doesn't fail
        projectRepository.save(testProject);
    }



    @Test
    void testFindById_NotFound() {
        Optional<Project> found = projectRepository.findById(9999);
        assertThat(found).isNotPresent();
    }

    @Test
    void testFindByProjectName_NotFound() {
        List<Project> found = projectRepository
                .findByProjectNameContainingIgnoreCase("xyznotexist");
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByUserID_NotFound() {
        List<Project> found = projectRepository.findByUser_UserID(9999);
        assertThat(found).isEmpty();
    }
}