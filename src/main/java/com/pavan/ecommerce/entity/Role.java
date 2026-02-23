package com.pavan.ecommerce.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity{

    @Column(unique = true)
    private String name; // ADMIN, USER
}
