package org.ecommerce.project.services;

import org.ecommerce.project.payloads.DTOs.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long userId, Long productId, Integer quantity);
}