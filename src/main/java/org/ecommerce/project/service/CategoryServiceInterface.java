package org.ecommerce.project.service;

import org.ecommerce.project.payload.DTOs.CategoryDTO;
import org.ecommerce.project.payload.responses.CategoryResponse;

public interface CategoryServiceInterface {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long id);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
}
