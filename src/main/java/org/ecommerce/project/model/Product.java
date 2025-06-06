package org.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 70, message = "Title must have between 5 and 70 characters")
    private String title;

    @NotNull
    @DecimalMin(value = "0.01", message = "Price must be equal or greater than R$ 0.01")
    private Double price;

    @DecimalMin(value = "0.01", message = "Special price must be equal or greater than R$ 0.01")
    private Double specialPrice;

    @DecimalMin(value = "0", message = "Discount must be be equal or greater than R$ 0.00")
    private Double discount;

    @NotBlank
    @Size(max = 1024, message = "Description must have less than 1024 characters")
    private String description;

    private String image;

    @NotNull
    @Max(value = 999, message = "Quantity must have less than 999 characters")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
