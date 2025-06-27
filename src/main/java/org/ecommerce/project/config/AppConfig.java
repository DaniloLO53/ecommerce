package org.ecommerce.project.config;

import org.ecommerce.project.models.CartProductMetadata;
import org.ecommerce.project.models.OrderItem;
import org.ecommerce.project.payloads.DTOs.CartProductMetadataDTO;
import org.ecommerce.project.payloads.DTOs.OrderDTO;
import org.ecommerce.project.payloads.DTOs.OrderItemDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Prevents OrderItem to receive the id from CartProductMetadata
        modelMapper.createTypeMap(CartProductMetadata.class, OrderItem.class)
                .addMappings(mapper -> {
                    mapper.skip(OrderItem::setId);
                });

        return modelMapper;
    }
}
