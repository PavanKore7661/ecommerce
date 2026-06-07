package com.pavan.ecommerce.controller;

import com.pavan.ecommerce.entity.Order;
import com.pavan.ecommerce.entity.Payment;
import com.pavan.ecommerce.enums.OrderStatus;
import com.pavan.ecommerce.enums.PaymentStatus;
import com.pavan.ecommerce.repository.OrderRepository;
import com.pavan.ecommerce.repository.PaymentRepository;
import com.pavan.ecommerce.service.EmailService;
import com.pavan.ecommerce.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public Payment initiatePayment(@RequestParam Long orderId,
                                  @RequestParam String method) {
     return paymentService.initiatePayment(orderId, method);
    }
}
