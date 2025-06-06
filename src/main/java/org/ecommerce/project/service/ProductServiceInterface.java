package org.ecommerce.project.service;

import org.ecommerce.project.payload.DTOs.ProductDTO;

public interface ProductServiceInterface {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
}
