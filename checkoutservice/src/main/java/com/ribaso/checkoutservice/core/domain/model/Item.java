package com.ribaso.checkoutservice.core.domain.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    @JsonBackReference
    private Basket basket;
}
