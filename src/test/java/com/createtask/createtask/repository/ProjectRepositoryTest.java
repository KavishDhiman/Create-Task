package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Project;
import com.createtask.createtask.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // uses your AWS MySQL
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository; // needed to attach a valid user

    private User testUser;
    private Project testProject;

    @BeforeEach
    void setup() {
        // Fetch an existing user from AWS DB (don't create new to avoid conflicts)
        testUser = userRepository.findById(1).orElseThrow();

        testProject = new Project();
        testProject.setProjectID(9001);
        testProject.setProjectName("Test Project");
        testProject.setDescription("Day 2 test");
        testProject.setStartDate(LocalDate.now());
        testProject.setUser(testUser);

        projectRepository.save(testProject);
    }

    @AfterEach
    void cleanup() {
        projectRepository.deleteById(9001);
    }


    @Test
    void testSaveProject() {
        Optional<Project> saved = projectRepository.findById(9001);
        assertThat(saved).isPresent();
        assertThat(saved.get().getProjectName()).isEqualTo("Test Project");
    }


    @Test
    void testFindAllProjects() {
        List<Project> all = projectRepository.findAll();
        assertThat(all).isNotEmpty();
    }


    @Test
    void testFindById() {
        Optional<Project> project = projectRepository.findById(9001);
        assertThat(project).isPresent();
        assertThat(project.get().getDescription()).isEqualTo("Day 2 test");
    }


    @Test
    void testUpdateProject() {
        Project p = projectRepository.findById(9001).orElseThrow();
        p.setProjectName("Updated Project");
        projectRepository.save(p);

        Project updated = projectRepository.findById(9001).orElseThrow();
        assertThat(updated.getProjectName()).isEqualTo("Updated Project");
    }


    @Test
    void testDeleteProject() {
        projectRepository.deleteById(9001);
        Optional<Project> deleted = projectRepository.findById(9001);
        assertThat(deleted).isEmpty();
    }


    @Test
    void testFindByUser() {
        List<Project> projects = projectRepository.findByUser_UserID(testUser.getUserID());
        assertThat(projects).isNotEmpty();
    }


    @Test
    void testFindByProjectNameKeyword() {
        List<Project> results = projectRepository.findByProjectNameContainingIgnoreCase("test");
        assertThat(results).isNotEmpty();
    }
}