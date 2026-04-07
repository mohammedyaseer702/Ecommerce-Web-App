package com.example.demo.controller;

import com.example.demo.dto.AdminOrderResponseDTO;
import com.example.demo.enums.OrderStatus;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@CrossOrigin
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminOrderResponseDTO>>> getAllOrders() {

        List<AdminOrderResponseDTO> orders = orderService.getAllOrders();

        return ResponseEntity.ok(
                ApiResponse.<List<AdminOrderResponseDTO>>builder()
                        .success(true)
                        .message("All orders fetched successfully")
                        .data(orders)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @RequestParam Long orderId,
            @RequestParam OrderStatus status,
            Authentication authentication) {

        String adminEmail = authentication.getName();

        String result = orderService.updateOrderStatus(
                orderId,
                status,
                adminEmail
        );

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Order status updated successfully")
                        .data(result)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/refund")
public ResponseEntity<ApiResponse<String>> refundOrder(
        @RequestParam Long orderId,
        @RequestParam String reason,
        Authentication authentication) {

    String adminEmail = authentication.getName();

    String result = orderService.refundOrder(
            orderId,
            reason,
            adminEmail
    );

    return ResponseEntity.ok(
            ApiResponse.<String>builder()
                    .success(true)
                    .message("Refund completed")
                    .data(result)
                    .timestamp(LocalDateTime.now())
                    .build()
    );
}

@PutMapping("/partial-refund")
public ResponseEntity<ApiResponse<String>> partialRefund(
        @RequestParam Long orderId,
        @RequestParam Long orderItemId,
        @RequestParam int quantity,
        @RequestParam String reason,
        Authentication authentication) {

    String adminEmail = authentication.getName();

    String result = orderService.partialRefund(
            orderId,
            orderItemId,
            quantity,
            reason,
            adminEmail
    );

    return ResponseEntity.ok(
            ApiResponse.<String>builder()
                    .success(true)
                    .message("Partial refund processed")
                    .data(result)
                    .timestamp(LocalDateTime.now())
                    .build()
    );
}


}
