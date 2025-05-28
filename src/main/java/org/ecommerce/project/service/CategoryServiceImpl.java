package org.ecommerce.project.service;

import org.ecommerce.project.exception.APIException;
import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;
import org.ecommerce.project.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public String createCategory(Category category) {
        Category savedCategory = categoryRepository.findByName(category.getName());
        if (savedCategory != null)
            throw new APIException("Category with name " + category.getName() + " already exists");
        categoryRepository.save(category);
        return "Category " + category.getName() + " created";
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setName(category.getName());
            return categoryRepository.save(existingCategory);
        } else {
            throw new ResourceNotFoundException("Category", "id", id);
        }
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(category);
        return "Category with id " + id + " has been deleted successfully";
    }
}
