package org.ecommerce.project.payloads.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long id;

    @NotBlank
    @Size(min = 5, message = "Street name must have at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 1, message = "Building name must have at least 1 characters")
    private String building;

    @NotBlank
    @Size(min = 3, message = "City name must have at least 3 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must have at least 2 characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must have at least 2 characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Zipcode name must have at least 6 characters")
    private String zipcode;
}
