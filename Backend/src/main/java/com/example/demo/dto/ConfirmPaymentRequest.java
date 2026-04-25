package com.example.demo.dto;

import lombok.Data;

@Data
public class ConfirmPaymentRequest {
    private Long orderId;
    private String transactionId;
    private boolean success;
}