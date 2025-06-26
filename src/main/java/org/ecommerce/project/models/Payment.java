package org.ecommerce.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    public Payment(Long id, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
        this.id = id;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean paid = false;

    @NotNull
    @Size(min = 4, message = "Payment must have at least 4 characters")
    private String paymentMethod;

    // pg = Payment Gateway
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    @OneToOne
    private Order order;
}
