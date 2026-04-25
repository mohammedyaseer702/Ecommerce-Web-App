package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.enums.OrderStatus;

@Data
@Builder
public class AdminOrderResponseDTO {

    private Long orderId;              // ✅ FIXED
    private String customerEmail;      // ✅ FIXED
    private LocalDateTime orderDate;   // ✅ Added
    private OrderStatus status;        // ✅ FIXED (enum)
    private String paymentMethod;
    private double totalAmount;

    private List<OrderItemDTO> items;  // ✅ IMPORTANT
}