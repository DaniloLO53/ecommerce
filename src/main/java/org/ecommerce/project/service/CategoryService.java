package org.ecommerce.project.service;

import org.ecommerce.project.exception.APIException;
import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.DTOs.CategoryDTO;
import org.ecommerce.project.payload.responses.CategoryResponse;
import org.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> page = categoryRepository.findAll(pageDetails);
        List<Category> categories = page.getContent();

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(page.getNumber());
        categoryResponse.setPageSize(page.getSize());
        categoryResponse.setTotalElements(page.getTotalElements());
        categoryResponse.setTotalPages(page.getTotalPages());
        categoryResponse.setLastPage(page.isLast());

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
