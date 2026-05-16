package com.createtask.createtask.service;

import com.createtask.createtask.dto.request.CategoryRequestDTO;
import com.createtask.createtask.dto.response.CategoryResponseDTO;

import java.util.List;

// Contract for category operations — per API spec, only Create and List are required
public interface CategoryService {

    // Creates a new category — throws 409 if a category with that ID already exists
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    // Retrieves all categories in the system — used by GET /api/v1/categories
    List<CategoryResponseDTO> getAllCategories();
}