package com.ribaso.checkoutservice.core.domain.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ribaso.checkoutservice.core.domain.model.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
}
