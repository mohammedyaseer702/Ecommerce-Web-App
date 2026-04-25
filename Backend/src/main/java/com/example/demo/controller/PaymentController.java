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

        Long orderId = Long.valueOf(request.get("orderId").toString());
Double amount = Double.valueOf(request.get("amount").toString());

String sessionUrl = stripeService.createCheckoutSession(orderId, amount);

return Map.of("url", sessionUrl);
    }
}