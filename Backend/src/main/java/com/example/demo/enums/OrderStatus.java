package com.example.demo.enums;

public enum OrderStatus {

    CREATED,
    PENDING,
    PLACED,            // Order placed but not processed
    PENDING_PAYMENT,    // Waiting for online payment
    PAID,               // Payment successful
    PAYMENT_FAILED,     // Payment failed
    CONFIRMED,          // Confirmed (COD orders)
    SHIPPED,            // Dispatched
    DELIVERED,          // Delivered to customer
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED
         // Cancelled by user or admin
}
