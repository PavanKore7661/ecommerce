package com.pavan.ecommerce.entity;

import com.pavan.ecommerce.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // INITIATED, SUCCESS, FAILED

    private String method; // CARD, UPI, NETBANKING
}