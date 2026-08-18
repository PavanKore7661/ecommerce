package com.pavan.ecommerce.service;

import com.pavan.ecommerce.dto.ProductRequest;
import com.pavan.ecommerce.entity.Category;
import com.pavan.ecommerce.entity.Product;
import com.pavan.ecommerce.repository.CategoryRepository;
import com.pavan.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Product updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById( request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public void uploadImage(Long id, MultipartFile image) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found"));
        try {
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(image.getInputStream(),uploadPath.resolve(image.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
            product.setImageUrl(image.getOriginalFilename());
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("Unable to upload image");

        }
    }
}
