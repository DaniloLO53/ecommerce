package org.ecommerce.project.payloads.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    public PaymentDTO(Long id, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
        this.id = id;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }

    private Long id;

    @NotNull
    private Boolean paid = false;

    @NotNull
    @Size(min = 4, message = "Payment must have at least 4 characters")
    private String paymentMethod;

    // pg = Payment Gateway
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;
}
