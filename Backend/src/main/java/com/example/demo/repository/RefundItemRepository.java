package com.example.demo.repository;

import com.example.demo.entity.RefundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefundItemRepository extends JpaRepository<RefundItem, Long> {

    @Query("""
           SELECT COALESCE(SUM(r.quantityRefunded), 0)
           FROM RefundItem r
           WHERE r.orderItem.id = :orderItemId
           """)
    int getTotalRefundedQuantity(@Param("orderItemId") Long orderItemId);
}
