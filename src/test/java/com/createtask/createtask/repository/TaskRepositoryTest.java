package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void saveAndFindTask() {
        Task task = new Task();
        task.setTaskID(9501);
        task.setTaskName("Test Task");
        task.setDescription("Testing save and find");
        task.setDueDate(LocalDate.of(2026, 5, 15));
        task.setPriority("High");
        task.setStatus("Pending");

        taskRepository.saveAndFlush(task);

        Task found = taskRepository.findById(9501).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getTaskName()).isEqualTo("Test Task");
        assertThat(found.getPriority()).isEqualTo("High");
    }

    @Test
    void findAllShouldContainSavedTask() {
        Task task = new Task();
        task.setTaskID(9502);
        task.setTaskName("Find All Task");
        taskRepository.saveAndFlush(task);

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks)
                .extracting(Task::getTaskID)
                .contains(9502);
    }

    @Test
    void updateTask() {
        Task task = new Task();
        task.setTaskID(9503);
        task.setTaskName("Old Name");
        taskRepository.saveAndFlush(task);

        Task saved = taskRepository.findById(9503).orElseThrow();
        saved.setTaskName("Updated Name");
        taskRepository.saveAndFlush(saved);

        Task updated = taskRepository.findById(9503).orElseThrow();
        assertThat(updated.getTaskName()).isEqualTo("Updated Name");
    }

    @Test
    void existsByIdShouldReturnTrueForSavedTask() {
        Task task = new Task();
        task.setTaskID(9504);
        task.setTaskName("Exists Task");
        taskRepository.saveAndFlush(task);

        assertThat(taskRepository.existsById(9504)).isTrue();
    }

    @Test
    void deleteByIdShouldRemoveTask() {
        Task task = new Task();
        task.setTaskID(9505);
        task.setTaskName("Delete Me");
        taskRepository.saveAndFlush(task);

        taskRepository.deleteById(9505);

        assertThat(taskRepository.findById(9505)).isNotPresent();
    }
}