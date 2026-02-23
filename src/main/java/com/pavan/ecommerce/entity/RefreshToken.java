package com.pavan.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken extends BaseEntity {

    @ManyToOne
    private User user;

    @Column(unique = true, nullable = false)
    private String token;

    private LocalDateTime expiryDate;

    private Boolean revoked = false;
}
