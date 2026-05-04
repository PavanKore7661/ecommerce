package com.pavan.ecommerce.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test/public")
    public String publicApi() {
        return "Public API - No authentication required";
    }

    @GetMapping("/api/test/user")
    @PreAuthorize("hasRole('USER')")
    public String userApi() {
        return "Hello USER - You are authenticated";
    }

    @GetMapping("/api/test/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "Hello ADMIN - You are authenticated";
    }
}
