package com.pavan.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private  JavaMailSender mailSender;

    @Async
    public void sendOrderConfirmation(String toEmail, Long orderId, Double amount) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Order Confirmation");

        message.setText(
                "Hello,\n\n" +
                "Your order #" + orderId + " has been placed successfully.\n" +
                "Total Amount: ₹" + amount + "\n\n" +
                "Thank you for shopping with us!"
        );

        mailSender.send(message);
    }
}