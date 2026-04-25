package com.example.demo.controller;

import com.example.demo.dto.OrderResponseDTO;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.enums.OrderStatus;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.AdminOrderResponseDTO;
import com.example.demo.dto.ConfirmPaymentRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> placeOrder(
            Authentication authentication,
            @RequestBody PlaceOrderRequest request) {

        String email = authentication.getName();

        OrderResponseDTO order =
                orderService.placeOrder(email, request.getPaymentMethod());

        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDTO>builder()
                        .success(true)
                        .message("Order placed successfully")
                        .data(order)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getMyOrders(
            Authentication authentication) {

        List<OrderResponseDTO> orders =
                orderService.getUserOrders(authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.<List<OrderResponseDTO>>builder()
                        .success(true)
                        .message("Orders fetched successfully")
                        .data(orders)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<String>> cancelOrder(
            @RequestParam Long orderId,
            Authentication authentication) {

        String result =
                orderService.cancelOrder(orderId, authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Order cancelled successfully")
                        .data(result)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }


    @GetMapping("/admin/all")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ApiResponse<List<AdminOrderResponseDTO>>> getAllOrders() {

    List<AdminOrderResponseDTO> orders = orderService.getAllOrders();

    return ResponseEntity.ok(
            ApiResponse.<List<AdminOrderResponseDTO>>builder()
                    .success(true)
                    .message("All orders fetched")
                    .data(orders)
                    .build()
    );
}

@PostMapping("/confirm-payment")
public ResponseEntity<ApiResponse<String>> confirmPayment(
        @RequestBody ConfirmPaymentRequest request) {

    String result = orderService.confirmPayment(
            request.getOrderId(),
            request.getTransactionId(),
            request.isSuccess()
    );

    return ResponseEntity.ok(
            ApiResponse.<String>builder()
                    .success(true)
                    .message("Payment confirmed")
                    .data(result)
                    .build()
    );
}



@PutMapping("/admin/update-status")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ApiResponse<String>> updateOrderStatus(
        @RequestParam("orderId") Long orderId,
        @RequestParam("status") OrderStatus status,
        Authentication authentication) {

    orderService.updateOrderStatus(orderId, status, authentication.getName());

    return ResponseEntity.ok(
            ApiResponse.<String>builder()
                    .success(true)
                    .message("Order status updated")
                    .data("Updated successfully")
                    .build()
    );
}


@GetMapping("/{id}")
public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(
        @PathVariable Long id,
        Authentication authentication) {

    OrderResponseDTO order =
            orderService.getOrderById(id, authentication.getName());

    return ResponseEntity.ok(
            ApiResponse.<OrderResponseDTO>builder()
                    .success(true)
                    .message("Order fetched successfully")
                    .data(order)
                    .timestamp(LocalDateTime.now())
                    .build()
    );
}

}