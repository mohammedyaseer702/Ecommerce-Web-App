package com.example.demo.enums;

public enum PaymentStatus {
    INITIATED,
    PROCESSING,
    PENDING,          // Payment created but not processed
    SUCCESS,          // Payment completed successfully
    FAILED,  
    CANCELLED,         // Payment failed
    REFUNDED ,
    PARTIALLY_REFUNDED         // Optional: for future refund feature
}
