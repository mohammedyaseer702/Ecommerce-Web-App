package com.example.demo.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeKey;

    public String createCheckoutSession(Long orderId, double amount) throws Exception {

    Stripe.apiKey = stripeKey;

    SessionCreateParams params =
            SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/payment-success?orderId=" + orderId)
                    .setCancelUrl("http://localhost:5173/cart")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount((long) (amount * 100))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order #" + orderId)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

    Session session = Session.create(params);

    return session.getUrl();
}
}