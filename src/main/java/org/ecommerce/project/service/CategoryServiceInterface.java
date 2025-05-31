package org.ecommerce.project.service;

import org.ecommerce.project.model.Category;

import java.util.List;

public interface CategoryServiceInterface {
    List<Category> getAllCategories();
    String createCategory(Category category);
    String deleteCategory(Long id);
    String updateCategory(Long id, Category category);
}
