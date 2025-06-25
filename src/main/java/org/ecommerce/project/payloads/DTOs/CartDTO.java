package org.ecommerce.project.payloads.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;

    @JsonIgnore
    private UserDTO user;

    private Set<CartProductMetadataDTO> cartsProductsMetadata = new HashSet<>();
}
