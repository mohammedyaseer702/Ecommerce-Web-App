package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.enums.OrderStatus;

@Data
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private OrderStatus status;
    private String paymentMethod;
    private List<OrderItemDTO> items;

}
