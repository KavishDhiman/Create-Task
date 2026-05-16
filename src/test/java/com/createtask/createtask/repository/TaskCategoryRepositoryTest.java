package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Category;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.TaskCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskCategoryRepositoryTest {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindTaskCategory() {
        Task task = new Task();
        task.setTaskID(9701);
        task.setTaskName("Task 9701");
        taskRepository.saveAndFlush(task);

        Category category = new Category();
        category.setCategoryID(9801);
        category.setCategoryName("Development");
        categoryRepository.saveAndFlush(category);

        TaskCategory.TaskCategoryId id = new TaskCategory.TaskCategoryId();
        id.setTaskID(9701);
        id.setCategoryID(9801);

        TaskCategory mapping = new TaskCategory();
        mapping.setId(id);
        mapping.setTask(task);
        mapping.setCategory(category);

        taskCategoryRepository.saveAndFlush(mapping);

        TaskCategory found = taskCategoryRepository.findById(id).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId().getTaskID()).isEqualTo(9701);
        assertThat(found.getId().getCategoryID()).isEqualTo(9801);
    }

    @Test
    void findAllShouldContainSavedMapping() {
        Task task = new Task();
        task.setTaskID(9702);
        task.setTaskName("Task 9702");
        taskRepository.saveAndFlush(task);

        Category category = new Category();
        category.setCategoryID(9802);
        category.setCategoryName("Testing");
        categoryRepository.saveAndFlush(category);

        TaskCategory.TaskCategoryId id = new TaskCategory.TaskCategoryId();
        id.setTaskID(9702);
        id.setCategoryID(9802);

        TaskCategory mapping = new TaskCategory();
        mapping.setId(id);
        mapping.setTask(task);
        mapping.setCategory(category);

        taskCategoryRepository.saveAndFlush(mapping);

        List<TaskCategory> mappings = taskCategoryRepository.findAll();

        assertThat(mappings)
                .extracting(tc -> tc.getId().getTaskID())
                .contains(9702);
    }

    @Test
    void deleteByIdShouldRemoveMapping() {
        Task task = new Task();
        task.setTaskID(9703);
        task.setTaskName("Task 9703");
        taskRepository.saveAndFlush(task);

        Category category = new Category();
        category.setCategoryID(9803);
        category.setCategoryName("Documentation");
        categoryRepository.saveAndFlush(category);

        TaskCategory.TaskCategoryId id = new TaskCategory.TaskCategoryId();
        id.setTaskID(9703);
        id.setCategoryID(9803);

        TaskCategory mapping = new TaskCategory();
        mapping.setId(id);
        mapping.setTask(task);
        mapping.setCategory(category);

        taskCategoryRepository.saveAndFlush(mapping);
        taskCategoryRepository.deleteById(id);

        assertThat(taskCategoryRepository.findById(id)).isNotPresent();
    }
}