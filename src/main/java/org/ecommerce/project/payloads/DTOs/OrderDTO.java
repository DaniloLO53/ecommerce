package org.ecommerce.project.payloads.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderDTO {
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private LocalDate orderDate;

    @NotNull
    @Positive
    private Double totalAmount;

    @ToString.Exclude
    private Set<OrderItemDTO> orderItems = new HashSet<>();

    private PaymentDTO payment;

    private AddressDTO address;
}
