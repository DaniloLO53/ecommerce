package org.ecommerce.project.repositories;

import org.ecommerce.project.models.Category;
import org.ecommerce.project.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageDetails);
    Page<Product> findByTitleLikeIgnoreCase(String keyword, Pageable pageDetails);
}
