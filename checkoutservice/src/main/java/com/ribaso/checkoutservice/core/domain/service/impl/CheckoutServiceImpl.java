package com.ribaso.checkoutservice.core.domain.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutRepository;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutService;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Override
    public Checkout initiateCheckout() {
        Checkout checkout = new Checkout();
        checkout.setStatus("pending");
        return checkoutRepository.save(checkout);
    }

    @Override
    public Checkout completeCheckout(Long checkoutId) {
        Checkout checkout = checkoutRepository.findById(checkoutId)
            .orElseThrow(() -> new InvalidCheckoutIdException("Invalid Checkout ID: " + checkoutId));

        if ("completed".equals(checkout.getStatus())) {
            throw new CheckoutAlreadyCompletedException("Checkout is already completed for ID: " + checkoutId);
        }

        checkout.setStatus("completed");
        return checkoutRepository.save(checkout);
    }
}