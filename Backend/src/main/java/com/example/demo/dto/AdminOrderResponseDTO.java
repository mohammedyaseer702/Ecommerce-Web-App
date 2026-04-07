package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.enums.OrderStatus;

@Data
@Builder
public class AdminOrderResponseDTO {

    private Long orderId;
    private String customerEmail;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String paymentMethod;
    private double totalAmount;
    private List<OrderItemDTO> items;
}
