package org.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30, message = "Category name must have between 3 and 30 characters")
    private String name;

    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Include
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();
}
