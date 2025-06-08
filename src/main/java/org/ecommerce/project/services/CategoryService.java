package org.ecommerce.project.services;

import org.ecommerce.project.payloads.DTOs.CategoryDTO;
import org.ecommerce.project.payloads.responses.PageResponse;

public interface CategoryService {
    PageResponse<CategoryDTO> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long id);
}
