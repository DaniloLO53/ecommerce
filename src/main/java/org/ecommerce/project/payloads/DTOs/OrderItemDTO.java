package org.ecommerce.project.payloads.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;

    @NotNull
    private ProductDTO product;

    @NotNull
    @JsonIgnore
    private OrderDTO order;

    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;
}
