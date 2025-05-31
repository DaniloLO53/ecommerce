package org.ecommerce.project.service;

import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;

public interface CategoryServiceInterface {
    CategoryResponse getAllCategories();
    CategoryDTO createCategory(CategoryDTO category);
    String deleteCategory(Long id);
    String updateCategory(Long id, Category category);
}
