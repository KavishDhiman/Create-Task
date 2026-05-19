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

// Tests for CategoryServiceImpl — Mockito is used so no real DB is touched
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    // Fake repository — all responses are controlled by us in each test
    @Mock private CategoryRepository categoryRepository;

    // Real service instance wired with the fake repository
    @InjectMocks private CategoryServiceImpl categoryService;

    // Reusable test data — rebuilt fresh before every single test
    private Category testCategory;
    private CategoryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Sample category entity matching DB script row (CategoryID=1, Development)
        testCategory = new Category();
        testCategory.setCategoryID(1);
        testCategory.setCategoryName("Development");

        // Sample request DTO — what the client sends when creating a category
        requestDTO = new CategoryRequestDTO();
        requestDTO.setCategoryID(1);
        requestDTO.setCategoryName("Development");
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 1 — createCategory: happy path
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("createCategory — saves the category and returns the correct DTO")
    void createCategory_success() {
        // Arrange: no duplicate exists, save works fine
        when(categoryRepository.existsById(1)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act
        CategoryResponseDTO result = categoryService.createCategory(requestDTO);

        // Assert: ID and name match what was sent
        assertThat(result.getCategoryID()).isEqualTo(1);
        assertThat(result.getCategoryName()).isEqualTo("Development");

        // Verify: save was called once
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 2 — createCategory: duplicate ID rejected
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("createCategory — throws 409 when a category with the same ID already exists")
    void createCategory_duplicateID() {
        // Arrange: category ID 1 already exists in DB
        when(categoryRepository.existsById(1)).thenReturn(true);

        // Act + Assert: must throw DuplicateResourceException
        assertThatThrownBy(() -> categoryService.createCategory(requestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Category already exists with ID: 1");

        // Verify: save must never be called when duplicate is found
        verify(categoryRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 3 — createCategory: returned name matches input
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("createCategory — returned category name matches the request input")
    void createCategory_returnedNameMatchesInput() {
        // Arrange
        when(categoryRepository.existsById(1)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act
        CategoryResponseDTO result = categoryService.createCategory(requestDTO);

        // Assert: the name in response is exactly what the client sent
        assertThat(result.getCategoryName()).isEqualTo(requestDTO.getCategoryName());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 4 — createCategory: save called exactly once
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("createCategory — repository save is called exactly once on success")
    void createCategory_saveCalledOnce() {
        // Arrange
        when(categoryRepository.existsById(1)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act
        categoryService.createCategory(requestDTO);

        // Assert: confirms data was actually persisted — not just returned from memory
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 5 — createCategory: different category also works
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("createCategory — a different category (Design) also saves correctly")
    void createCategory_differentCategory() {
        // Arrange: build request and entity for Design category (matches DB script ID=2)
        CategoryRequestDTO designRequest = new CategoryRequestDTO();
        designRequest.setCategoryID(2);
        designRequest.setCategoryName("Design");

        Category designCategory = new Category();
        designCategory.setCategoryID(2);
        designCategory.setCategoryName("Design");

        when(categoryRepository.existsById(2)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(designCategory);

        // Act
        CategoryResponseDTO result = categoryService.createCategory(designRequest);

        // Assert: Design category was saved and returned correctly
        assertThat(result.getCategoryID()).isEqualTo(2);
        assertThat(result.getCategoryName()).isEqualTo("Design");
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 6 — getAllCategories: returns all as list
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getAllCategories — returns all categories from DB as a list of DTOs")
    void getAllCategories_returnsList() {
        // Arrange: two categories exist in DB
        Category design = new Category();
        design.setCategoryID(2);
        design.setCategoryName("Design");

        when(categoryRepository.findAll()).thenReturn(List.of(testCategory, design));

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert: two items returned with correct names
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Development");
        assertThat(result.get(1).getCategoryName()).isEqualTo("Design");
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 7 — getAllCategories: empty DB
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getAllCategories — returns empty list when no categories exist in DB")
    void getAllCategories_emptyDB() {
        // Arrange: nothing in DB
        when(categoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert: empty list — service should not throw an error for this
        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 8 — getAllCategories: list size matches DB count
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getAllCategories — list size matches the exact number of records in DB")
    void getAllCategories_sizeMatchesDB() {
        // Arrange: three categories in DB
        Category c2 = new Category(); c2.setCategoryID(2); c2.setCategoryName("Design");
        Category c3 = new Category(); c3.setCategoryID(3); c3.setCategoryName("Marketing");

        when(categoryRepository.findAll()).thenReturn(List.of(testCategory, c2, c3));

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert: exactly 3 DTOs returned — one per DB record
        assertThat(result).hasSize(3);
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 9 — getAllCategories: first item has correct ID
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getAllCategories — first item in the list has the correct category ID")
    void getAllCategories_firstItemCorrectID() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory));

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert: first (and only) item has categoryID = 1
        assertThat(result.get(0).getCategoryID()).isEqualTo(1);
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 10 — createCategory: duplicate check always runs before save
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("createCategory — existsById is checked before save is ever attempted")
    void createCategory_duplicateCheckRunsBeforeSave() {
        // Arrange: duplicate exists — save should never be reached
        when(categoryRepository.existsById(1)).thenReturn(true);

        // Act: expect the exception
        assertThatThrownBy(() -> categoryService.createCategory(requestDTO))
                .isInstanceOf(DuplicateResourceException.class);

        // Assert: existsById ran once and save was completely skipped
        verify(categoryRepository, times(1)).existsById(1);
        verify(categoryRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════
    // TEST 11 — getAllCategories: findAll called exactly once
    // ══════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getAllCategories — repository findAll is called exactly once per request")
    void getAllCategories_findAllCalledOnce() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory));

        // Act
        categoryService.getAllCategories();

        // Assert: no extra redundant DB calls — findAll fires once
        verify(categoryRepository, times(1)).findAll();
    }
}