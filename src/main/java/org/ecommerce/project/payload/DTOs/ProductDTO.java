package org.ecommerce.project.payload.DTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;

    @NotNull
    @DecimalMin(value = "0.01", message = "Price must be equal or greater than R$ 0.01")
    private Double price;

    @DecimalMin(value = "0.01", message = "Discount must be equal or greater than R$ 0.01")
    private Double discount;

    @DecimalMin(value = "0.01", message = "Special price must be equal or greater than R$ 0.01")
    private Double specialPrice;

    @NotBlank
    @Size(min = 5, max = 70, message = "Title must have between 5 and 70 characters")
    private String title;

    @NotBlank
    @Size(max = 1024, message = "Description must have less than 1024 characters")
    private String description;

    private String image;

    @Max(value = 999, message = "Quantity must have less than 999 characters")
    private Integer quantity;
}
