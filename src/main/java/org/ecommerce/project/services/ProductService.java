package org.ecommerce.project.services;

import org.ecommerce.project.payloads.DTOs.ProductDTO;
import org.ecommerce.project.payloads.responses.PageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    PageResponse<ProductDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    ProductDTO deleteProduct(Long productId);
    PageResponse<ProductDTO> getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    PageResponse<ProductDTO> getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductDTO updateProductImage(Long productId, MultipartFile image);
}
