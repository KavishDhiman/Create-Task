package com.createtask.createtask.service.impl;

import com.createtask.createtask.dto.request.CategoryRequestDTO;
import com.createtask.createtask.dto.response.CategoryResponseDTO;
import com.createtask.createtask.entity.Category;
import com.createtask.createtask.exception.DuplicateResourceException;
import com.createtask.createtask.repository.CategoryRepository;
import com.createtask.createtask.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Business logic implementation for category creation and retrieval
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    // Handles all Category DB operations
    private final CategoryRepository categoryRepository;

    // Constructor injection for CategoryRepository
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Checks for duplicate ID first, then builds and saves the category entity
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        // Reject if a category with this ID already exists — clearer than a DB constraint error
        if (categoryRepository.existsById(requestDTO.getCategoryID())) {
            throw new DuplicateResourceException(
                    "Category already exists with ID: " + requestDTO.getCategoryID());
        }

        // Map DTO to entity and persist
        Category category = new Category();
        category.setCategoryID(requestDTO.getCategoryID());
        category.setCategoryName(requestDTO.getCategoryName());

        return mapToResponseDTO(categoryRepository.save(category));
    }

    // Retrieves all categories — readOnly for performance (no dirty checking needed)
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Converts a Category entity to a CategoryResponseDTO
    private CategoryResponseDTO mapToResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategoryID(category.getCategoryID());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }
}