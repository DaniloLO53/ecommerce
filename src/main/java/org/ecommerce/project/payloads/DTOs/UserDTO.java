package org.ecommerce.project.payloads.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank
    @Size(max = 20, message = "Username must have less than 20 characters")
    private String username;

    @NotBlank
    @Size(max = 50, message = "Email must have less than 50 characters")
    private String email;

    @NotBlank
    private String password;

    private Set<RoleDTO> role;

    private Set<ProductDTO> product;

    private List<AddressDTO> address;

    private CartDTO cart;
}
