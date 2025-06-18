package org.ecommerce.project.payloads.responses;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20, message = "Username must have between 3 and 20 characters")
    private String username;

    @NotBlank
    @Size(max = 50, message = "Email must have less than 50 characters")
    @Email
    private String email;

    private Set<String> roles;

    @NotBlank
    private String password;
}
