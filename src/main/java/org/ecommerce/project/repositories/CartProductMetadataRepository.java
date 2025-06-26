package org.ecommerce.project.repositories;

import org.ecommerce.project.models.CartProductMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductMetadataRepository extends JpaRepository<CartProductMetadata, Long> {
    Optional<CartProductMetadata> findByCart_User_idAndProduct_Id(Long userId, Long productId);

    List<CartProductMetadata> findAllByCart_User_Id(Long cartUserId);

    @Modifying(clearAutomatically = true)
    int deleteByCart_User_idAndProduct_Id(Long userId, Long productId);

    @Modifying(clearAutomatically = true)
    int deleteAllByCart_Id(Long cartId);
}
