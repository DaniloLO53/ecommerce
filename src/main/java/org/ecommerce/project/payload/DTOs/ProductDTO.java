package org.ecommerce.project.payload.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank
    @DecimalMin(value = "0.01", message = "Price must be greater than R$ 0.01")
    private Double price;

    @NotBlank
    @DecimalMin(value = "0.01", message = "Discount must be greater than R$ 0.01")
    private Double discount;

    @NotBlank
    @DecimalMin(value = "0.01", message = "Special price must be greater than R$ 0.01")
    private Double specialPrice;

    @NotBlank
    @Size(min = 5, max = 70, message = "Title must have between 5 and 70 characters")
    private String title;

    @NotBlank
    @Size(max = 1024, message = "Description must have less than 1024 characters")
    private String description;

    @NotBlank
    private String image;

    @NotBlank
    @Size(max = 999, message = "Quantity must have less than 999 characters")
    private Integer quantity;
}
