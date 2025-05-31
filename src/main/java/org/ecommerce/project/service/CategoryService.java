package org.ecommerce.project.service;

import org.ecommerce.project.exception.APIException;
import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryServiceInterface {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public String createCategory(Category category) {
        Category exitstingCategory = categoryRepository.findByName(category.getName());
        if (exitstingCategory == null) {
            categoryRepository.save(category);
            return "Category '" + category.getName() + "' has been created";
        }
        throw new APIException("Category with name " + category.getName() + " already exists");
    }

    @Override
    public String deleteCategory(Long id) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(id);
        if (optionalSavedCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return "Category with id " + id + " has been deleted";
        }
        throw new ResourceNotFoundException("Category", "id", id);
    }

    @Override
    public String updateCategory(Long id, Category category) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(id);
        if (optionalSavedCategory.isPresent()) {
            Category savedCategory = optionalSavedCategory.get();
            savedCategory.setName(category.getName());
            categoryRepository.save(savedCategory);
            return "Category with id " + id + " has been updated";
        }
        throw new ResourceNotFoundException("Category", "id", id);
    }
}
