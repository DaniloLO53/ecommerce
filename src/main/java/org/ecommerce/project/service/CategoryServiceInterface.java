package org.ecommerce.project.service;

import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryServiceInterface {
    CategoryResponse getAllCategories();
    String createCategory(Category category);
    String deleteCategory(Long id);
    String updateCategory(Long id, Category category);
}
