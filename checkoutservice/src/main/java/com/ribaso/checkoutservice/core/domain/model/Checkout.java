package com.ribaso.checkoutservice.core.domain.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Data
@Entity
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status; // Example: pending, completed

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basket_id", referencedColumnName = "id")
    @JsonManagedReference
    private Basket basket;
}
