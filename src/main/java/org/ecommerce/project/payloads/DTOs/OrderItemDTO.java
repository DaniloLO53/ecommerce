package org.ecommerce.project.payloads.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemDTO {
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private ProductDTO product;

    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;
}
