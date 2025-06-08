package org.ecommerce.project.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.ecommerce.project.config.AppConstants;
import org.ecommerce.project.payloads.DTOs.CategoryDTO;
import org.ecommerce.project.payloads.responses.PageResponse;
import org.ecommerce.project.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(categoryId, categoryDTO));
    }

    @DeleteMapping("/public/categories/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.deleteCategory(id));
    }

    @GetMapping("/public/categories")
    public ResponseEntity<PageResponse<CategoryDTO>> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.Category.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.Category.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.Category.SORT_BY, required = false)
            @Pattern(regexp = "id|name", message = "sortBy field must have values: 'id' or 'name'") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.Category.SORT_ORDER, required = false) String sortOrder
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder));
    }
}
