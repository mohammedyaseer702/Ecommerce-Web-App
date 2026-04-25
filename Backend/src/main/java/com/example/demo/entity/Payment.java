package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.example.demo.enums.PaymentStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private String gatewayResponse;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Double amount;

    private String paymentMethod; // ONLINE, COD

    @Enumerated(EnumType.STRING) 
    private PaymentStatus status;
    // SUCCESS, FAILED, PENDING

    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(name = "order_id" , nullable = false)
    private Order order;
}
