package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {

    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private double subtotal;
    private String imageUrl;
}
