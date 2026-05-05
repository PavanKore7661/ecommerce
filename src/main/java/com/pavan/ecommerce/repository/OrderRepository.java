package com.pavan.ecommerce.repository;

import com.pavan.ecommerce.entity.Order;
import com.pavan.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}