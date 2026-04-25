package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Payment payment;


    private double totalRefundAmount;

    private double amount;

    private String reason;

    @Column(unique = true)
private String refundTransactionId;


    private LocalDateTime refundedAt;

    private String processedBy; // admin email
}
