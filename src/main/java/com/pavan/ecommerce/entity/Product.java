package com.pavan.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    private String name;

    private String description;

    private Double price;

    private Integer stockQuantity;
    private Double rating;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
