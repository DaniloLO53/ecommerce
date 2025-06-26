package org.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private LocalDate orderDate;

    @NotNull
    @Positive
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne(mappedBy = "order", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Payment payment;

    @ManyToOne
    private Address address;
}
