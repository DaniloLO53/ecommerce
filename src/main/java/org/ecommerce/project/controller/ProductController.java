package org.ecommerce.project.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.ecommerce.project.config.AppConstants;
import org.ecommerce.project.config.enums.ProductSortBy;
import org.ecommerce.project.payload.DTOs.ProductDTO;
import org.ecommerce.project.payload.responses.ProductResponse;
import org.ecommerce.project.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false)
            @Pattern(regexp = "id|price", message = "sortBy field must have values: 'id' or 'price'") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDirection
            ) {
        return ResponseEntity.ok(productService.getAllProducts(pageNumber, pageSize, sortBy, sortDirection));
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false)
            @Pattern(regexp = "id|price", message = "sortBy field must have values: 'id' or 'price'") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDirection
            ) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDirection));
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId, @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(categoryId, productDTO));
    }
}
