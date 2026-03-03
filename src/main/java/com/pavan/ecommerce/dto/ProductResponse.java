package com.pavan.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String categoryName;
}