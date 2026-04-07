package com.example.demo.controller;

import com.example.demo.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final StripeService stripeService;

    @PostMapping("/create-session")
    public Map<String, String> createSession(@RequestBody Map<String, Long> request) throws Exception {

        Long amount = request.get("amount");

        String sessionId = stripeService.createCheckoutSession(amount);

        return Map.of("sessionId", sessionId);
    }
}