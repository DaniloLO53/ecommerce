package org.ecommerce.project.repositories;

import org.ecommerce.project.models.Cart;
import org.ecommerce.project.payloads.DTOs.CartDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartByUser_Id(Long userId);
}
