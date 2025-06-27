package org.ecommerce.project.repositories;

import org.ecommerce.project.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
