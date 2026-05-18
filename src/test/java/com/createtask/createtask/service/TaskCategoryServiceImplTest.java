package com.createtask.createtask.service;

import com.createtask.createtask.dto.response.CategoryResponseDTO;
import com.createtask.createtask.entity.Category;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.TaskCategory;
import com.createtask.createtask.entity.User;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.exception.ResourceNotFoundException;
import com.createtask.createtask.repository.CategoryRepository;
import com.createtask.createtask.repository.TaskCategoryRepository;
import com.createtask.createtask.repository.TaskRepository;
import com.createtask.createtask.service.impl.TaskCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

// Isolates TaskCategoryServiceImpl — all three repositories are mocked, no DB touched
@ExtendWith(MockitoExtension.class)
class TaskCategoryServiceImplTest {

    // Mock repositories — none of these hit the real database
    @Mock private TaskRepository taskRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private TaskCategoryRepository taskCategoryRepository;

    // Inject all three mocks into the real service implementation
    @InjectMocks private TaskCategoryServiceImpl taskCategoryService;

    // Shared fixtures rebuilt before every test
    private Task testTask;
    private Category testCategory;
    private TaskCategory testMapping;
    private TaskCategory.TaskCategoryId compositeKey;

    @BeforeEach
    void setUp() {
        // Build a minimal user needed by the Task entity
        User user = new User();
        user.setUserID(1);
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setEmail("john@email.com");
        user.setFullName("John Doe");

        // Build the task entity used across mapping tests
        testTask = new Task();
        testTask.setTaskID(1);
        testTask.setTaskName("Task One");
        testTask.setDueDate(LocalDate.of(2022, 1, 10));
        testTask.setPriority("High");
        testTask.setStatus("In Progress");
        testTask.setUser(user);

        // Build the category entity used across mapping tests
        testCategory = new Category();
        testCategory.setCategoryID(2);
        testCategory.setCategoryName("Design");

        // Build the composite key representing the task-category pairing
        compositeKey = new TaskCategory.TaskCategoryId();
        compositeKey.setTaskID(1);
        compositeKey.setCategoryID(2);

        // Build the full mapping entity linking the task and category
        testMapping = new TaskCategory();
        testMapping.setId(compositeKey);
        testMapping.setTask(testTask);
        testMapping.setCategory(testCategory);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // assignCategoryToTask
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("assignCategoryToTask — positive: creates mapping and returns success message")
    void assignCategoryToTask_success() {
        // Arrange: both entities exist and no duplicate mapping is present
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(testCategory));
        when(taskCategoryRepository.existsByTaskIDAndCategoryID(1, 2)).thenReturn(false);
        when(taskCategoryRepository.save(any(TaskCategory.class))).thenReturn(testMapping);

        // Act: assign category 2 to task 1
        String result = taskCategoryService.assignCategoryToTask(1, 2);

        // Assert: result contains a meaningful confirmation with category name and task ID
        assertThat(result).contains("Design");
        assertThat(result).contains("1");

        // Verify the mapping row was saved exactly once
        verify(taskCategoryRepository, times(1)).save(any(TaskCategory.class));
    }

    @Test
    @DisplayName("assignCategoryToTask — negative: throws 404 when task does not exist")
    void assignCategoryToTask_taskNotFound() {
        // Arrange: task lookup returns empty — task 99 does not exist
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        // Act + Assert: expect 404 with task-specific message
        assertThatThrownBy(() -> taskCategoryService.assignCategoryToTask(99, 2))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");

        // Save must never fire when the task doesn't exist
        verify(taskCategoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("assignCategoryToTask — negative: throws 404 when category does not exist")
    void assignCategoryToTask_categoryNotFound() {
        // Arrange: task exists but category 99 does not
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        // Act + Assert: expect 404 with category-specific message
        assertThatThrownBy(() -> taskCategoryService.assignCategoryToTask(1, 99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID: 99");

        // Save must never fire when the category doesn't exist
        verify(taskCategoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("assignCategoryToTask — negative: throws 409 when mapping already exists")
    void assignCategoryToTask_duplicateMapping() {
        // Arrange: both entities exist but this exact mapping already exists in DB
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(testCategory));
        when(taskCategoryRepository.existsByTaskIDAndCategoryID(1, 2)).thenReturn(true);

        // Act + Assert: service must throw 409 to prevent duplicate rows
        assertThatThrownBy(() -> taskCategoryService.assignCategoryToTask(1, 2))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already assigned");

        // Save must never fire on a duplicate
        verify(taskCategoryRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // removeCategoryFromTask
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("removeCategoryFromTask — positive: deletes mapping and returns confirmation")
    void removeCategoryFromTask_success() {
        // Arrange: the mapping exists and can be found by composite key
        when(taskCategoryRepository.findById(compositeKey)).thenReturn(Optional.of(testMapping));

        // Act: remove category 2 from task 1
        String result = taskCategoryService.removeCategoryFromTask(1, 2);

        // Assert: result message references both the category and task IDs
        assertThat(result).contains("2");
        assertThat(result).contains("1");

        // Verify the mapping row was deleted exactly once
        verify(taskCategoryRepository, times(1)).delete(testMapping);
    }

    @Test
    @DisplayName("removeCategoryFromTask — negative: throws 404 when mapping does not exist")
    void removeCategoryFromTask_mappingNotFound() {
        // Arrange: the composite key lookup returns empty — mapping was never created
        when(taskCategoryRepository.findById(any(TaskCategory.TaskCategoryId.class)))
                .thenReturn(Optional.empty());

        // Act + Assert: deleting a non-existent mapping must throw 404
        assertThatThrownBy(() -> taskCategoryService.removeCategoryFromTask(1, 2))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Mapping not found");

        // Delete must never be called when the mapping doesn't exist
        verify(taskCategoryRepository, never()).delete(any());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getCategoriesForTask
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getCategoriesForTask — positive: returns all categories linked to the task")
    void getCategoriesForTask_success() {
        // Arrange: task exists and has one category mapping
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskCategoryRepository.findByTaskID(1)).thenReturn(List.of(testMapping));

        // Act
        List<CategoryResponseDTO> result = taskCategoryService.getCategoriesForTask(1);

        // Assert: one category returned with correct name and ID
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Design");
        assertThat(result.get(0).getCategoryID()).isEqualTo(2);
    }

    @Test
    @DisplayName("getCategoriesForTask — positive: returns empty list when task has no categories")
    void getCategoriesForTask_noCategories() {
        // Arrange: task exists but no categories have been assigned yet
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskCategoryRepository.findByTaskID(1)).thenReturn(List.of());

        // Act
        List<CategoryResponseDTO> result = taskCategoryService.getCategoriesForTask(1);

        // Assert: empty list returned cleanly — not an error condition
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getCategoriesForTask — negative: throws 404 when task does not exist")
    void getCategoriesForTask_taskNotFound() {
        // Arrange: task 99 does not exist
        when(taskRepository.existsById(99)).thenReturn(false);

        // Act + Assert: service must throw 404 before querying categories
        assertThatThrownBy(() -> taskCategoryService.getCategoriesForTask(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");

        // Category lookup must never fire when the task doesn't exist
        verify(taskCategoryRepository, never()).findByTaskID(anyInt());
    }
}