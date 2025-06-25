package org.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CartProductMetadata> cartProductMetadata;
}
