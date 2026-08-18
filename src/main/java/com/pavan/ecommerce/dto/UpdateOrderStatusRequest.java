package com.pavan.ecommerce.dto;
import com.pavan.ecommerce.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateOrderStatusRequest {

    private OrderStatus status;
}
