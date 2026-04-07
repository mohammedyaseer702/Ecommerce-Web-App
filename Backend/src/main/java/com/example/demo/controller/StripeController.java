package com.example.demo.controller;

import com.example.demo.service.StripeService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:5173")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-session")
    public Map<String, String> createSession(@RequestBody Map<String, Long> request) throws Exception {

        Long amount = request.get("amount");

        String url = stripeService.createCheckoutSession(amount);

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", url); // returning URL

        return response;
    }
}