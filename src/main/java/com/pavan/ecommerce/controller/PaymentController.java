package com.pavan.ecommerce.controller;

import com.pavan.ecommerce.entity.Order;
import com.pavan.ecommerce.entity.Payment;
import com.pavan.ecommerce.enums.OrderStatus;
import com.pavan.ecommerce.enums.PaymentStatus;
import com.pavan.ecommerce.repository.OrderRepository;
import com.pavan.ecommerce.repository.PaymentRepository;
import com.pavan.ecommerce.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping("/initiate")
    public String initiatePayment(@RequestParam Long orderId,
                                  @RequestParam String method) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        orderRepository.save(order);

        Payment payment = Payment.builder()
                .order(order)
                .paymentId(UUID.randomUUID().toString())
                .status(PaymentStatus.INITIATED)
                .method(method)
                .build();

        paymentRepository.save(payment);
        log.info("Payment initiated for Payment Id: {}", payment.getId());
        return payment.getPaymentId();
    }

    @PostMapping("/complete")
    public String completePayment(@RequestParam String paymentId, @RequestParam boolean success) {

        Payment payment = paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Order order = payment.getOrder();

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PAID);
            emailService.sendOrderConfirmation(
                    order.getUser().getEmail(),
                    order.getId(),
                    order.getTotalAmount()
            );
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.FAILED);
        }

        paymentRepository.save(payment);
        orderRepository.save(order);
        log.info("Payment Completed for Payment Id: {}", payment.getId());
        return success ? "Payment Successful" : "Payment Failed";
    }
}
