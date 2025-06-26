package org.ecommerce.project.controllers;

import org.ecommerce.project.payloads.DTOs.OrderDTO;
import org.ecommerce.project.payloads.responses.OrderRequest;
import org.ecommerce.project.security.services.UserDetailsImpl;
import org.ecommerce.project.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(
            Authentication authentication,
            @RequestBody OrderRequest orderRequest,
            @PathVariable String paymentMethod) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.orderProducts(userId, orderRequest, paymentMethod));
    }
}
