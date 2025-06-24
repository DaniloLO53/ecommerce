package org.ecommerce.project.payloads.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;

    private UserDTO user;

    private Set<CartProductMetadataDTO> cartProductMetadataDTOS;
}
