package com.example.demo.util;

import com.example.demo.enums.OrderStatus;

import java.util.*;

public class OrderStateMachine {

    private static final Map<OrderStatus, Set<OrderStatus>> allowedTransitions = new HashMap<>();

    static {

        // 🟢 Created → waiting for payment (online)
        allowedTransitions.put(OrderStatus.CREATED,
                Set.of(OrderStatus.PENDING_PAYMENT, OrderStatus.CANCELLED));

        // 🟡 Waiting for payment
        allowedTransitions.put(OrderStatus.PENDING_PAYMENT,
                Set.of(OrderStatus.PAID,
                       OrderStatus.PAYMENT_FAILED,
                       OrderStatus.CANCELLED));

        // 🔴 Payment failed
        allowedTransitions.put(OrderStatus.PAYMENT_FAILED,
                Set.of(OrderStatus.CANCELLED));

        // 💰 Paid (online success)
        allowedTransitions.put(OrderStatus.PAID,
                Set.of(OrderStatus.SHIPPED,
                       OrderStatus.CANCELLED));

        // 📦 COD confirmed
        allowedTransitions.put(OrderStatus.CONFIRMED,
                Set.of(OrderStatus.SHIPPED,
                       OrderStatus.CANCELLED));

        // 🚚 Shipped
        allowedTransitions.put(OrderStatus.SHIPPED,
                Set.of(OrderStatus.DELIVERED));

        // 📬 Delivered (terminal)
        allowedTransitions.put(OrderStatus.DELIVERED,
                Set.of());

        // ❌ Cancelled (terminal)
        allowedTransitions.put(OrderStatus.CANCELLED,
                Set.of());
    }

    public static boolean canTransition(OrderStatus current,
                                        OrderStatus target) {

        return allowedTransitions
                .getOrDefault(current, Collections.emptySet())
                .contains(target);
    }
}
