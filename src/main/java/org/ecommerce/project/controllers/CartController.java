package org.ecommerce.project.controllers;

import org.ecommerce.project.payloads.DTOs.CartDTO;
import org.ecommerce.project.security.services.UserDetailsImpl;
import org.ecommerce.project.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}")
    public ResponseEntity<CartDTO> addProductToCart(
            Authentication authentication,
            @PathVariable Long productId,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addProductToCart(userId, productId, quantity));
    }
}
