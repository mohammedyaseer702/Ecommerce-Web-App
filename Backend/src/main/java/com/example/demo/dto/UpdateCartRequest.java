package com.example.demo.dto;

import lombok.Data;

@Data
public class UpdateCartRequest {
    private Long productId;
    private int quantity;
}