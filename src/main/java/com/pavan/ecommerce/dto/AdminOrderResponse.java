package com.pavan.ecommerce.dto;

import com.pavan.ecommerce.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdminOrderResponse {

    private Long id;

    private String customerName;

    private Double totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private Integer itemCount;
}