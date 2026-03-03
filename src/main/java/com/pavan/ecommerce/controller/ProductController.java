package com.pavan.ecommerce.controller;

import com.pavan.ecommerce.dto.ProductRequest;
import com.pavan.ecommerce.dto.ProductResponse;
import com.pavan.ecommerce.entity.Category;
import com.pavan.ecommerce.entity.Product;
import com.pavan.ecommerce.repository.CategoryRepository;
import com.pavan.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(@RequestBody ProductRequest productReq) {

        Long categoryId = productReq.getCategoryId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(productReq.getName())
                .description(productReq.getDescription())
                .price(productReq.getPrice())
                .stockQuantity(productReq.getStockQuantity())
                .category(category)
                .build();

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .categoryName(product.getCategory().getName())
                .build();
    }

    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {

        Sort sorting = Sort.by(
                Sort.Direction.fromString(sort[1]),
                sort[0]
        );

        Pageable pageable = PageRequest.of(page, size, sorting);

        return productRepository.findAll(pageable);
    }

    @GetMapping("/search")
    public Page<Product> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @GetMapping("/filter")
    public Page<ProductResponse> filterProducts(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "newest") String sort
    ) {

        Sort sorting;

        switch (sort.toLowerCase()) {
            case "price_asc":
                sorting = Sort.by("price").ascending();
                break;
            case "price_desc":
                sorting = Sort.by("price").descending();
                break;
            case "newest":
            default:
                sorting = Sort.by("createdAt").descending();
                break;
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Specification<Product> spec =
                Specification.where(ProductSpecification.hasMinPrice(minPrice))
                        .and(ProductSpecification.hasMaxPrice(maxPrice))
                        .and(ProductSpecification.hasCategory(categoryId))
                        .and(ProductSpecification.hasMinRating(rating));

        Page<Product> products = productRepository.findAll(spec, pageable);

        return products.map(this::mapToResponse);
    }
}
