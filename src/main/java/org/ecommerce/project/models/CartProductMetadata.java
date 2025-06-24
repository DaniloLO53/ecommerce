package org.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Set;

@Entity(name = "carts_products_metadata")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CartProductMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @ManyToOne
    private Product product;

    @Positive(message = "Quantity must be a positive number")
    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private Integer quantity;

    @ManyToOne
    private Cart cart;
}
