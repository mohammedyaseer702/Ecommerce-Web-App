package com.example.demo.util;

import com.example.demo.enums.PaymentStatus;

import java.util.*;

public class PaymentStateMachine {

    private static final Map<PaymentStatus, Set<PaymentStatus>> allowedTransitions = new HashMap<>();

    static {
        allowedTransitions.put(PaymentStatus.PENDING,
                Set.of(PaymentStatus.SUCCESS, PaymentStatus.FAILED));

        allowedTransitions.put(PaymentStatus.SUCCESS,
                Set.of());

        allowedTransitions.put(PaymentStatus.FAILED,
                Set.of());
    }

    public static boolean canTransition(PaymentStatus current, PaymentStatus target) {
        return allowedTransitions
                .getOrDefault(current, Collections.emptySet())
                .contains(target);
    }
}
