package com.pavan.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double totalAmount;

    private String status; // CREATED, PAID, SHIPPED

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}