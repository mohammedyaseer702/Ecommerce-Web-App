package com.example.demo.repository;

import com.example.demo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
     @Query("""
        SELECT DISTINCT c 
        FROM Cart c
        LEFT JOIN FETCH c.items i
        LEFT JOIN FETCH i.product
        WHERE c.user.email = :email
    """)
    Optional<Cart> findByUserEmailWithItems(@Param("email") String email);
    Optional<Cart> findByUser(User user);
}
