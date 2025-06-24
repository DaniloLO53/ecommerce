package org.ecommerce.project.repositories;

import org.ecommerce.project.models.CartProductMetadata;
import org.ecommerce.project.payloads.DTOs.CartProductMetadataDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductMetadataRepository extends JpaRepository<CartProductMetadata, Long> {
}
