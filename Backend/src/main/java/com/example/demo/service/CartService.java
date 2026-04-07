package com.example.demo.service;

import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.CartResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // ================= ADD TO CART =================

    public Cart addToCart(String email, Long productId, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (product.getStock() < quantity) {
            throw new BadRequestException("Not enough stock available");
        }

        Cart cart = cartRepository.findByUserEmailWithItems(email)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {

            int newQuantity = existingItem.getQuantity() + quantity;

            if (product.getStock() < newQuantity) {
                throw new BadRequestException("Stock exceeded");
            }

            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);

        } else {

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

            cart.getItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

        return cart;
    }

    // ================= GET CART =================

    public Cart getUserCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return cartRepository.findByUserEmailWithItems(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));
    }

    // ================= UPDATE QUANTITY =================

    public Cart updateQuantity(String email, Long productId, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        Cart cart = getUserCart(email);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not in cart"));

        if (item.getProduct().getStock() < quantity) {
            throw new BadRequestException("Stock exceeded");
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return cart;
    }

    // ================= REMOVE ITEM =================

    public Cart removeItem(String email, Long productId) {

        Cart cart = getUserCart(email);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not in cart"));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        return cart;
    }

    // ================= CALCULATE TOTAL =================

    public double calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // ================= CONVERT TO DTO =================

    public CartResponseDTO convertToDto(Cart cart) {

        var itemDTOs = cart.getItems().stream()
                .map(item -> CartItemDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .toList();

        double total = itemDTOs.stream()
                .mapToDouble(CartItemDTO::getSubtotal)
                .sum();

        return CartResponseDTO.builder()
                .cartId(cart.getId())
                .items(itemDTOs)
                .totalAmount(total)
                .build();
    }
}

