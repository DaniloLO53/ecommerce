package org.ecommerce.project.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.ecommerce.project.config.AppConstants;
import org.ecommerce.project.payloads.DTOs.ProductDTO;
import org.ecommerce.project.payloads.responses.PageResponse;
import org.ecommerce.project.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public ResponseEntity<PageResponse<ProductDTO>> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.Product.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.Product.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.Product.SORT_BY, required = false)
            @Pattern(regexp = "id|price", message = "sortBy field must have values: 'id' or 'price'") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.Product.SORT_ORDER, required = false) String sortOrder
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<PageResponse<ProductDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.Product.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.Product.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.Product.SORT_BY, required = false)
            @Pattern(regexp = "id|price", message = "sortBy field must have values: 'id' or 'price'") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.Product.SORT_ORDER, required = false) String sortOrder
    ) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<PageResponse<ProductDTO>> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.Product.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.Product.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.Product.SORT_BY, required = false)
            @Pattern(regexp = "id|price", message = "sortBy field must have values: 'id' or 'price'") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.Product.SORT_ORDER, required = false) String sortOrder
    ) {
        return ResponseEntity.ok(productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder));
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId, @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(categoryId, productDTO));
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId, productDTO));
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(productId));
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam(name = "image") MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProductImage(productId, image));
    }
}
