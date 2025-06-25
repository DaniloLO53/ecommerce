package org.ecommerce.project.repositories;

import org.ecommerce.project.models.CartProductMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductMetadataRepository extends JpaRepository<CartProductMetadata, Long> {
    Optional<CartProductMetadata> findByCart_User_idAndProduct_Id(Long userId, Long productId);
    void deleteByCart_User_idAndProduct_Id(Long userId, Long productId);
    List<CartProductMetadata> findByProduct_Id(Long productId);
}
