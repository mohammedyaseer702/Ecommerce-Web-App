package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"refund_id", "order_item_id"}
    )
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Refund refund;

    @ManyToOne
    private OrderItem orderItem;

    private int quantityRefunded;

    private double refundAmount;
}
