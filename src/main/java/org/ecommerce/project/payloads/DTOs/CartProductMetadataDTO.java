package org.ecommerce.project.payloads.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductMetadataDTO {
    private Long id;

    private ProductDTO productDTO;

    private Integer quantity;

    private Set<CartDTO> cartDTOS;
}
