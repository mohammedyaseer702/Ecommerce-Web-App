package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
       SELECT p FROM Product p
       WHERE p.active = true
       AND (:category IS NULL OR p.category = :category)
       AND (:minPrice IS NULL OR p.price >= :minPrice)
       AND (:maxPrice IS NULL OR p.price <= :maxPrice)
       """)
Page<Product> filterProducts(
        @Param("category") String category,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);
    
}
