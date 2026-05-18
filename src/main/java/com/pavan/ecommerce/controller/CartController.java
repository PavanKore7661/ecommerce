package com.pavan.ecommerce.controller;

import com.pavan.ecommerce.entity.Cart;
import com.pavan.ecommerce.entity.CartItem;
import com.pavan.ecommerce.entity.Product;
import com.pavan.ecommerce.entity.User;
import com.pavan.ecommerce.repository.CartItemRepository;
import com.pavan.ecommerce.repository.CartRepository;
import com.pavan.ecommerce.repository.ProductRepository;
import com.pavan.ecommerce.repository.UserRepository;
import com.pavan.ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {

        String email = getLoggedInUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().user(user).build()
                ));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }

        cartItemRepository.save(cartItem);

        return "Product added to cart";
    }

    @GetMapping
    public List<CartItem> viewCart() {

        String email = getLoggedInUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cartItemRepository.findByCart(cart);
    }

    @DeleteMapping("/clear")
    public String clearCart() {

        String email = getLoggedInUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cartItemRepository.findByCart(cart);

        cartItemRepository.deleteAll(items);

        return "Cart cleared";
    }
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable Long id) {

        String email = getLoggedInUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        // Security check
        if (!item.getCart().getId().equals(cart.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("This item does not belong to your cart");
        }
        cartItemRepository.delete(item);
        return ResponseEntity.ok("Product removed from cart");
    }

    @PutMapping("/increase/{cartItemId}")
    public ResponseEntity<?> increaseQuantity( @PathVariable Long cartItemId) {

        String email = getLoggedInUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Increase quantity by 1
        cartItem.setQuantity(cartItem.getQuantity() + 1);

        cartItemRepository.save(cartItem);

        return ResponseEntity.ok("Quantity increased");
    }

    @PutMapping("/decrease/{cartItemId}")
    public ResponseEntity<?> decreaseQuantity(@PathVariable Long cartItemId) {

        String email = getLoggedInUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository
                .findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        int currentQty = cartItem.getQuantity();

        // If quantity becomes 0,
        // remove item from cart

        if (currentQty <= 1) {

            cartItemRepository.delete(cartItem);

            return ResponseEntity.ok("Product removed from cart");
        }

        // Decrease quantity

        cartItem.setQuantity(currentQty - 1);

        cartItemRepository.save(cartItem);

        return ResponseEntity.ok("Quantity decreased");
    }
}
