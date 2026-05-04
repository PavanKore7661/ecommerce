package com.pavan.ecommerce.repository;

import com.pavan.ecommerce.entity.Cart;
import com.pavan.ecommerce.entity.CartItem;
import com.pavan.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    List<CartItem> findByCart(Cart cart);
}
