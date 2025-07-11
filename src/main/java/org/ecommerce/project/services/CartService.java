package org.ecommerce.project.services;

import org.ecommerce.project.payloads.DTOs.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long userId, Long productId, Integer quantity);
    List<CartDTO> getAllCarts();
    CartDTO getUserCart(Long userId);
    CartDTO updateProductQuantity(Long userId, Long productId, Integer quantity);
    String deleteProductFromCart(Long userId, Long productId);
}