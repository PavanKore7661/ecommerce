package com.pavan.ecommerce.controller;

import com.pavan.ecommerce.entity.*;
import com.pavan.ecommerce.enums.OrderStatus;
import com.pavan.ecommerce.repository.*;
import com.pavan.ecommerce.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/place")
    public String placeOrder() {
      return orderService.placeOrder();
    }

    @GetMapping
    public List<Order> getUserOrders(Authentication authentication) {
        return orderService.getUserOrders(authentication.getName());
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

}
