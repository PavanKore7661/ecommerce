package com.pavan.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="cart")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
