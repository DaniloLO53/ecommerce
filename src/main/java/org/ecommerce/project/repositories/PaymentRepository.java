package org.ecommerce.project.repositories;

import org.ecommerce.project.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
