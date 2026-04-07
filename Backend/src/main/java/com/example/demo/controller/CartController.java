package com.example.demo.controller;

import com.example.demo.dto.CartResponseDTO;
import com.example.demo.dto.AddToCartRequest;
import com.example.demo.dto.UpdateCartRequest;
import com.example.demo.entity.Cart;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(
            Authentication authentication,
            @RequestBody AddToCartRequest request) {

        String email = authentication.getName();

        Cart cart = cartService.addToCart(
                email,
                request.getProductId(),
                request.getQuantity()
        );

        CartResponseDTO dto = cartService.convertToDto(cart);

        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .success(true)
                        .message("Product added to cart successfully")
                        .data(dto)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> viewCart(
            Authentication authentication) {

        String email = authentication.getName();
        Cart cart = cartService.getUserCart(email);
        CartResponseDTO dto = cartService.convertToDto(cart);

        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .success(true)
                        .message("Cart fetched successfully")
                        .data(dto)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateQuantity(
            Authentication authentication,
            @RequestBody UpdateCartRequest request) {

        String email = authentication.getName();

        Cart cart = cartService.updateQuantity(
                email,
                request.getProductId(),
                request.getQuantity()
        );

        CartResponseDTO dto = cartService.convertToDto(cart);

        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .success(true)
                        .message("Cart updated successfully")
                        .data(dto)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeItem(
            Authentication authentication,
            @RequestParam Long productId) {

        String email = authentication.getName();
        Cart cart = cartService.removeItem(email, productId);
        CartResponseDTO dto = cartService.convertToDto(cart);

        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .success(true)
                        .message("Item removed from cart successfully")
                        .data(dto)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}