package com.ribaso.checkoutservice.core.domain.service.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ribaso.checkoutservice.core.domain.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
}
