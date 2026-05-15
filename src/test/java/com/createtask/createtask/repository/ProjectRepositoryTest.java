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
import java.util.TreeSet;

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
        if (projectRepository.existsById(9002)) {
            projectRepository.deleteById(9002);
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


    @Test
    void testEquals_SameProjectID_ShouldBeEqual() {
        Project p1 = new Project();
        p1.setProjectID(9001);

        Project p2 = new Project();
        p2.setProjectID(9001);

        assertThat(p1).isEqualTo(p2);
    }

    @Test
    void testEquals_DifferentProjectID_ShouldNotBeEqual() {
        Project p1 = new Project();
        p1.setProjectID(9001);

        Project p2 = new Project();
        p2.setProjectID(9002);

        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    void testHashCode_SameProjectID_ShouldHaveSameHashCode() {
        Project p1 = new Project();
        p1.setProjectID(9001);

        Project p2 = new Project();
        p2.setProjectID(9001);

        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }



    @Test
    void testCompareTo_ShouldSortByStartDateAscending() {
        User user = userRepository.findById(1).get();

        Project early = new Project();
        early.setProjectID(9001);
        early.setProjectName("Early Project");
        early.setStartDate(LocalDate.of(2024, 1, 1));
        early.setUser(user);

        Project late = new Project();
        late.setProjectID(9002);
        late.setProjectName("Late Project");
        late.setStartDate(LocalDate.of(2024, 6, 1));
        late.setUser(user);

        assertThat(early.compareTo(late)).isNegative();
    }

    @Test
    void testCompareTo_EqualDates_ShouldReturnZero() {
        Project p1 = new Project();
        p1.setProjectID(9001);
        p1.setStartDate(LocalDate.of(2024, 1, 1));

        Project p2 = new Project();
        p2.setProjectID(9002);
        p2.setStartDate(LocalDate.of(2024, 1, 1));

        assertThat(p1.compareTo(p2)).isZero();
    }

    @Test
    void testTreeSet_ShouldStoreSortedByStartDate() {
        User user = userRepository.findById(1).get();

        Project p1 = new Project();
        p1.setProjectID(9001);
        p1.setProjectName("March Project");
        p1.setStartDate(LocalDate.of(2024, 3, 1));
        p1.setUser(user);

        Project p2 = new Project();
        p2.setProjectID(9002);
        p2.setProjectName("January Project");
        p2.setStartDate(LocalDate.of(2024, 1, 1));
        p2.setUser(user);

        TreeSet<Project> sortedProjects = new TreeSet<>();
        sortedProjects.add(p1);
        sortedProjects.add(p2);

        assertThat(sortedProjects.first().getProjectName())
                .isEqualTo("January Project");
    }
}