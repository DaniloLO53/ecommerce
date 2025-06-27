package org.ecommerce.project.repositories;

import org.ecommerce.project.models.CartProductMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductMetadataRepository extends JpaRepository<CartProductMetadata, Long> {
    Optional<CartProductMetadata> findByCart_User_idAndProduct_Id(Long userId, Long productId);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM carts_products_metadata WHERE cart_id = ?1", nativeQuery = true)
    void deleteAllByCart_Id(Long cartId);
}
