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
    @OneToOne
    private Product product;

    @Positive
    private Long quantity;

    @ManyToMany(mappedBy = "cartProductMetadata")
    @ToString.Exclude
    private Set<Cart> carts;
}
