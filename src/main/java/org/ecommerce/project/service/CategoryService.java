package org.ecommerce.project.service;

import org.ecommerce.project.exception.APIException;
import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;
import org.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryServiceInterface {
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if (existingCategory == null) {
            Category savedCategory = categoryRepository.save(category);
            return modelMapper.map(savedCategory, CategoryDTO.class);
        }
        throw new APIException("Category with name " + existingCategory.getName() + " already exists");
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(id);
        if (optionalSavedCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return modelMapper.map(optionalSavedCategory.get(), CategoryDTO.class);
        }
        throw new ResourceNotFoundException("Category", "id", id);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(id);
        if (optionalSavedCategory.isPresent()) {
            Category category = modelMapper.map(categoryDTO, Category.class);
            category.setId(id);
            Category newSavedCategory = categoryRepository.save(category);
            return modelMapper.map(newSavedCategory, CategoryDTO.class);
        }
        throw new ResourceNotFoundException("Category", "id", id);
    }
}
