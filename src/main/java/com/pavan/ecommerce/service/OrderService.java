package com.pavan.ecommerce.service;

import com.pavan.ecommerce.controller.OrderController;
import com.pavan.ecommerce.entity.*;
import com.pavan.ecommerce.enums.OrderStatus;
import com.pavan.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public String placeOrder(){
        String email = jwtService.getLoggedInUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = 0;

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        com.pavan.ecommerce.entity.Payment payment =paymentService.initiatePayment(order.getId(), "ONLINE");
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());

            total += item.getQuantity() * item.getProduct().getPrice();

            orderItems.add(orderItem);
        }

        order.setTotalAmount(total);
        order.setItems(orderItems);

        orderRepository.save(order);
        paymentService.completePayment(payment.getPaymentId(), true);
        // Clear cart
        cartItemRepository.deleteAll(cartItems);
        log.info("Order created for user: {}", email);
        return "Order placed successfully";
    }
    public List<Order> getUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    public Order getOrderById( Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->new RuntimeException("Order not found"));
    }
}
