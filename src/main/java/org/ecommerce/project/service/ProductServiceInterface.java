package org.ecommerce.project.service;

import org.ecommerce.project.payload.DTOs.ProductDTO;
import org.ecommerce.project.payload.responses.ProductResponse;

public interface ProductServiceInterface {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
