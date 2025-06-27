package org.ecommerce.project.services;

import org.ecommerce.project.payloads.DTOs.OrderDTO;
import org.ecommerce.project.payloads.responses.OrderRequest;

public interface OrderService {
    OrderDTO orderProducts(Long userId, OrderRequest orderRequest, String paymentMethod);
}
