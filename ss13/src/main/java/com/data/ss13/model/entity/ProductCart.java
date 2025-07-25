package com.data.ss13.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "product_cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private Integer quantity;
}
