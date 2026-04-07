package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.RefreshTokenService;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil; // ✅ FIXED
    private final RefreshTokenService refreshTokenService;

    // ================= REGISTER =================

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(
            @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(user)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody LoginRequest request) {

        User user = authService.authenticate(request);

        String accessToken = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getName()
        );

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken.getToken());

        return ResponseEntity.ok(
                ApiResponse.<Map<String, String>>builder()
                        .success(true)
                        .message("Login successful")
                        .data(tokens)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ================= REFRESH =================

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(
            @RequestBody Map<String, String> request) {

        String requestToken = request.get("refreshToken");

        RefreshToken refreshToken =
                refreshTokenService.verifyToken(requestToken);

        String newAccessToken = jwtUtil.generateToken(
                refreshToken.getUser().getEmail(),
                refreshToken.getUser().getRole().getName()
        );

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(
                ApiResponse.<Map<String, String>>builder()
                        .success(true)
                        .message("Token refreshed successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
