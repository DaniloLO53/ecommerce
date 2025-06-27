package org.ecommerce.project.payloads.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductMetadataDTO {
    public CartProductMetadataDTO(ProductDTO product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    private Long id;

    @NotNull
    private ProductDTO product;

    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;
}
