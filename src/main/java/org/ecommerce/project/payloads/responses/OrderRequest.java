package org.ecommerce.project.payloads.responses;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ecommerce.project.payloads.DTOs.AddressDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private AddressDTO address;

    @NotNull
    @Size(min = 4, message = "Payment must have at least 4 characters")
    private String paymentMethod;

    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;
}
