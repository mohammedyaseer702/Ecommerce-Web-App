package com.example.demo.controller;

import com.example.demo.payload.ApiResponse;
import com.example.demo.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // 1️⃣ Forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestParam String email) {

        String token = passwordResetService.generateResetToken(email);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Reset token generated")
                        .data(token) // later remove this
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // 2️⃣ Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        String message =
                passwordResetService.resetPassword(token, newPassword);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
