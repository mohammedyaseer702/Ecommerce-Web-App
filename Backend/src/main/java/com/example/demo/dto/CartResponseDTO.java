package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponseDTO {

    private Long cartId;
    private List<CartItemDTO> items;
    private double totalAmount;
}
