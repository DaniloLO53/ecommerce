package org.ecommerce.project.repository;

import org.ecommerce.project.model.Category;
import org.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageDetails);
    Page<Product> findByTitleLikeIgnoreCase(String keyword, Pageable pageDetails);
}
