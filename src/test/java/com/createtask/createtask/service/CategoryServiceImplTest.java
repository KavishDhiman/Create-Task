package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.CategoryRequestDTO;
import com.createtask.createtask.dto.response.CategoryResponseDTO;
import com.createtask.createtask.entity.Category;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.repository.CategoryRepository;
import com.createtask.createtask.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Isolates CategoryServiceImpl using Mockito — no Spring context needed, tests run instantly
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    // Mock CategoryRepository so no real DB call is made
    @Mock private CategoryRepository categoryRepository;

    // Inject the mock into a real CategoryServiceImpl instance
    @InjectMocks private CategoryServiceImpl categoryService;

    // Shared test fixtures — rebuilt fresh before every test to avoid state bleed
    private Category testCategory;
    private CategoryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Build a sample Category entity used across multiple test cases
        testCategory = new Category();
        testCategory.setCategoryID(1);
        testCategory.setCategoryName("Development");

        // Build the matching request DTO for create operations
        requestDTO = new CategoryRequestDTO();
        requestDTO.setCategoryID(1);
        requestDTO.setCategoryName("Development");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // createCategory
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("createCategory — positive: saves category and returns correct DTO")
    void createCategory_success() {
        // Arrange: no existing category with this ID, save returns the built entity
        when(categoryRepository.existsById(1)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act: call the service method under test
        CategoryResponseDTO result = categoryService.createCategory(requestDTO);

        // Assert: returned DTO fields match the saved entity
        assertThat(result.getCategoryID()).isEqualTo(1);
        assertThat(result.getCategoryName()).isEqualTo("Development");

        // Verify repository save was invoked exactly once
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("createCategory — negative: throws 409 when category ID already exists")
    void createCategory_duplicateID() {
        // Arrange: simulate a category with this ID already present in the DB
        when(categoryRepository.existsById(1)).thenReturn(true);

        // Act + Assert: service must throw DuplicateResourceException with a clear message
        assertThatThrownBy(() -> categoryService.createCategory(requestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Category already exists with ID: 1");

        // Save must never be called if a duplicate is detected
        verify(categoryRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getAllCategories
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllCategories — positive: returns all categories as DTOs")
    void getAllCategories_success() {
        // Build a second category to test multi-item list mapping
        Category second = new Category();
        second.setCategoryID(2);
        second.setCategoryName("Design");

        // Arrange: repository returns two categories
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory, second));

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert: list size and values match what the repository returned
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Development");
        assertThat(result.get(1).getCategoryName()).isEqualTo("Design");
    }

    @Test
    @DisplayName("getAllCategories — positive: returns empty list when no categories exist")
    void getAllCategories_empty() {
        // Arrange: repository returns nothing — valid state when no data is seeded yet
        when(categoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert: empty list returned cleanly — no exception thrown
        assertThat(result).isEmpty();
    }
}