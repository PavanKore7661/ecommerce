package com.pavan.ecommerce.repository;

import com.pavan.ecommerce.entity.Cart;
import com.pavan.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}