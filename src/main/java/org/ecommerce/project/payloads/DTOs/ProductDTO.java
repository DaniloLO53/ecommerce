package org.ecommerce.project.payloads.DTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100, message = "Title must have between 3 and 100 characters")
    private String title;

    @Size(max = 1000, message = "Description must have less than 1000 characters")
    private String description;

    @NotNull
    @Max(value = 999, message = "Quantity must have less than 999 characters")
    private Integer quantity;

    @DecimalMin(value = "1", message = "Price must be greater than 1")
    private Double price;

    private String image;
}
