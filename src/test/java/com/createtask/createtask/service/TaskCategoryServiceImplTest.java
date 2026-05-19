package com.createtask.createtask.service;

import com.createtask.createtask.dto.response.CategoryResponseDTO;
import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.entity.Category;
import com.createtask.createtask.entity.Task;
import com.createtask.createtask.entity.TaskCategory;
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
import static org.mockito.Mockito.*;

// Tests for TaskCategoryServiceImpl — covers assign, remove, and list of task-category links
@ExtendWith(MockitoExtension.class)
class TaskCategoryServiceImplTest {

    // All three repositories are faked — no actual DB calls happen
    @Mock private TaskRepository taskRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private TaskCategoryRepository taskCategoryRepository;

    // Real service wired with the fake repositories
    @InjectMocks private TaskCategoryServiceImpl taskCategoryService;

    // Shared test data rebuilt before every test
    private Task testTask;
    private Category testCategory;
    private TaskCategory testMapping;
    private TaskCategory.TaskCategoryId compositeKey;

    @BeforeEach
    void setUp() {
        // Minimal AppUser to satisfy the Task entity — matches renamed entity
        AppUser user = new AppUser();
        user.setUserID(1);
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setEmail("john.doe@email.com");
        user.setFullName("John Doe");

        // Task entity — matches Task table row from DB script (TaskID=1)
        testTask = new Task();
        testTask.setTaskID(1);
        testTask.setTaskName("Task One");
        testTask.setDueDate(LocalDate.of(2022, 1, 10));
        testTask.setPriority("High");
        testTask.setStatus("In Progress");
        testTask.setUser(user);

        // Category entity — matches DB script (CategoryID=2, Design)
        testCategory = new Category();
        testCategory.setCategoryID(2);
        testCategory.setCategoryName("Design");

        // Composite primary key for the TaskCategory join table
        compositeKey = new TaskCategory.TaskCategoryId();
        compositeKey.setTaskID(1);
        compositeKey.setCategoryID(2);

        // Full TaskCategory mapping row — links task 1 to category 2
        testMapping = new TaskCategory();
        testMapping.setId(compositeKey);
        testMapping.setTask(testTask);
        testMapping.setCategory(testCategory);
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 1 — assignCategoryToTask: happy path
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("assignCategoryToTask — links category to task and returns success message")
    void assignCategoryToTask_success() {
        // Arrange: both exist, no duplicate
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(testCategory));
        when(taskCategoryRepository.existsByTaskIDAndCategoryID(1, 2)).thenReturn(false);
        when(taskCategoryRepository.save(any(TaskCategory.class))).thenReturn(testMapping);

        // Act
        String result = taskCategoryService.assignCategoryToTask(1, 2);

        // Assert: message confirms the category name and task ID
        assertThat(result).contains("Design");
        assertThat(result).contains("1");

        // Verify: mapping row was saved exactly once
        verify(taskCategoryRepository, times(1)).save(any(TaskCategory.class));
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 2 — assignCategoryToTask: task not found
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("assignCategoryToTask — throws 404 when the task does not exist")
    void assignCategoryToTask_taskNotFound() {
        // Arrange: task 99 missing from DB
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        // Act + Assert: must throw 404 with task ID in message
        assertThatThrownBy(() -> taskCategoryService.assignCategoryToTask(99, 2))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");

        // Verify: save never fires when task is missing
        verify(taskCategoryRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 3 — assignCategoryToTask: category not found
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("assignCategoryToTask — throws 404 when the category does not exist")
    void assignCategoryToTask_categoryNotFound() {
        // Arrange: task exists but category 99 missing
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        // Act + Assert: must throw 404 with category ID in message
        assertThatThrownBy(() -> taskCategoryService.assignCategoryToTask(1, 99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID: 99");

        // Verify: save never fires when category is missing
        verify(taskCategoryRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 4 — assignCategoryToTask: duplicate mapping rejected
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("assignCategoryToTask — throws 409 when the mapping already exists")
    void assignCategoryToTask_duplicateMapping() {
        // Arrange: both exist but this pair is already linked in DB
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(testCategory));
        when(taskCategoryRepository.existsByTaskIDAndCategoryID(1, 2)).thenReturn(true);

        // Act + Assert: must throw 409 to prevent duplicate row
        assertThatThrownBy(() -> taskCategoryService.assignCategoryToTask(1, 2))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already assigned");

        // Verify: save never fires on a duplicate
        verify(taskCategoryRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 5 — assignCategoryToTask: save called once on success
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("assignCategoryToTask — repository save is called exactly once on success")
    void assignCategoryToTask_saveCalledOnce() {
        // Arrange: everything valid
        when(taskRepository.findById(1)).thenReturn(Optional.of(testTask));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(testCategory));
        when(taskCategoryRepository.existsByTaskIDAndCategoryID(1, 2)).thenReturn(false);
        when(taskCategoryRepository.save(any(TaskCategory.class))).thenReturn(testMapping);

        // Act
        taskCategoryService.assignCategoryToTask(1, 2);

        // Assert: confirms the mapping row was actually persisted
        verify(taskCategoryRepository, times(1)).save(any(TaskCategory.class));
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 6 — removeCategoryFromTask: happy path
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("removeCategoryFromTask — removes the mapping and returns a confirmation message")
    void removeCategoryFromTask_success() {
        // Arrange: the mapping row exists in DB
        when(taskCategoryRepository.findById(compositeKey)).thenReturn(Optional.of(testMapping));

        // Act
        String result = taskCategoryService.removeCategoryFromTask(1, 2);

        // Assert: message confirms which category and task were involved
        assertThat(result).contains("2");
        assertThat(result).contains("1");

        // Verify: delete was called exactly once
        verify(taskCategoryRepository, times(1)).delete(testMapping);
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 7 — removeCategoryFromTask: mapping not found
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("removeCategoryFromTask — throws 404 when the mapping does not exist")
    void removeCategoryFromTask_notFound() {
        // Arrange: composite key lookup returns nothing
        when(taskCategoryRepository.findById(any(TaskCategory.TaskCategoryId.class)))
                .thenReturn(Optional.empty());

        // Act + Assert: must throw 404 — can't remove a non-existent mapping
        assertThatThrownBy(() -> taskCategoryService.removeCategoryFromTask(1, 2))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Mapping not found");

        // Verify: delete must never fire when mapping doesn't exist
        verify(taskCategoryRepository, never()).delete(any());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 8 — removeCategoryFromTask: delete called once
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("removeCategoryFromTask — repository delete is called exactly once on success")
    void removeCategoryFromTask_deleteCalledOnce() {
        // Arrange: mapping found
        when(taskCategoryRepository.findById(compositeKey)).thenReturn(Optional.of(testMapping));

        // Act
        taskCategoryService.removeCategoryFromTask(1, 2);

        // Assert: actual DB row deletion happened exactly once
        verify(taskCategoryRepository, times(1)).delete(testMapping);
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 9 — getCategoriesForTask: happy path
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getCategoriesForTask — returns all categories linked to the task")
    void getCategoriesForTask_success() {
        // Arrange: task exists and has one category linked (Design)
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskCategoryRepository.findByTaskID(1)).thenReturn(List.of(testMapping));

        // Act
        List<CategoryResponseDTO> result = taskCategoryService.getCategoriesForTask(1);

        // Assert: one category returned with correct name and ID
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Design");
        assertThat(result.get(0).getCategoryID()).isEqualTo(2);
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 10 — getCategoriesForTask: no categories assigned yet
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getCategoriesForTask — returns empty list when no categories are assigned")
    void getCategoriesForTask_noCategories() {
        // Arrange: task exists but no category linked
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskCategoryRepository.findByTaskID(1)).thenReturn(List.of());

        // Act
        List<CategoryResponseDTO> result = taskCategoryService.getCategoriesForTask(1);

        // Assert: empty list — not an error, just means no categories assigned yet
        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 11 — getCategoriesForTask: task not found
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getCategoriesForTask — throws 404 when the task does not exist")
    void getCategoriesForTask_taskNotFound() {
        // Arrange: task 99 does not exist
        when(taskRepository.existsById(99)).thenReturn(false);

        // Act + Assert: must throw 404 before even querying categories
        assertThatThrownBy(() -> taskCategoryService.getCategoriesForTask(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");

        // Verify: findByTaskID never fires when task doesn't exist
        verify(taskCategoryRepository, never()).findByTaskID(anyInt());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 12 — getCategoriesForTask: multiple categories returned
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getCategoriesForTask — returns multiple categories when task has more than one")
    void getCategoriesForTask_multipleCategories() {
        // Build a second category — Testing (matches DB CategoryID=4)
        Category testing = new Category();
        testing.setCategoryID(4);
        testing.setCategoryName("Testing");

        // Build a second mapping for task 1 → category 4
        TaskCategory.TaskCategoryId secondKey = new TaskCategory.TaskCategoryId();
        secondKey.setTaskID(1);
        secondKey.setCategoryID(4);

        TaskCategory secondMapping = new TaskCategory();
        secondMapping.setId(secondKey);
        secondMapping.setTask(testTask);
        secondMapping.setCategory(testing);

        // Arrange: task has two categories linked
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskCategoryRepository.findByTaskID(1))
                .thenReturn(List.of(testMapping, secondMapping));

        // Act
        List<CategoryResponseDTO> result = taskCategoryService.getCategoriesForTask(1);

        // Assert: both categories returned with correct names
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Design");
        assertThat(result.get(1).getCategoryName()).isEqualTo("Testing");
    }
}