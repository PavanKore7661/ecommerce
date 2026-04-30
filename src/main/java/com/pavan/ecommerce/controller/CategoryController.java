package com.pavan.ecommerce.controller;

import com.pavan.ecommerce.entity.Category;
import com.pavan.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // Create category (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(@RequestBody Category category) {

        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category already exists");
        }
        return categoryRepository.save(category);
    }

    // Get all categories (PUBLIC)
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
