package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private double price;

    private int quantity;

    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
@JoinColumn(name = "product_id")
private Product product;

}
