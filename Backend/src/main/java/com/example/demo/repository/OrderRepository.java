package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ For user order history
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findByUser(User user);

    // ✅ Default findById (returns Optional)
    Optional<Order> findById(Long id);

    // ✅ Pessimistic locking version (for refund / critical updates)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdForUpdate(@Param("id") Long id);
}
