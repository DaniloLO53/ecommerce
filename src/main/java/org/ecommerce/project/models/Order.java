package org.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private LocalDate orderDate;

    @NotNull
    @Positive
    private Double totalAmount;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;

    @OneToOne(mappedBy = "order", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private Payment payment;

    @ManyToOne
    private Address address;
}
