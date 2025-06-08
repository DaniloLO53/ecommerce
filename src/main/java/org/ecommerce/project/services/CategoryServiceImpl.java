package org.ecommerce.project.services;

import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.Category;
import org.ecommerce.project.payloads.DTOs.CategoryDTO;
import org.ecommerce.project.payloads.responses.PageResponse;
import org.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(categoryId);

        if (optionalSavedCategory.isPresent()) {
            Category category = modelMapper.map(categoryDTO, Category.class);
            category.setId(categoryId);
            Category savedCategory = categoryRepository.save(category);
            return modelMapper.map(savedCategory, CategoryDTO.class);
        }

        throw new ResourceNotFoundException("Category", "id", categoryId);
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
    public PageResponse<CategoryDTO> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> page = categoryRepository.findAll(pageDetails);
        List<Category> categories = page.getContent();

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        PageResponse<CategoryDTO> categoryResponse = new PageResponse<>();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(page.getNumber());
        categoryResponse.setPageSize(page.getSize());
        categoryResponse.setTotalElements(page.getTotalElements());
        categoryResponse.setTotalPages(page.getTotalPages());
        categoryResponse.setLastPage(page.isLast());

        return categoryResponse;
    }
}
