package com.ribaso.checkoutservice.core.domain.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ribaso.checkoutservice.core.domain.model.Basket;
import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutRepository;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutService;
import com.ribaso.checkoutservice.port.basket.GetBasket;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private CheckoutRepository checkoutRepository;

     @Autowired
    private GetBasket getBasket;


    @Override
    public Checkout initiateCheckout(String id) {
        Checkout checkout = new Checkout();
        checkout.setStatus("pending");

        // Fetch the current basket
        Basket basket = getBasket.getBasket(id);
        checkout.setBasket(basket);
        System.out.println("Basket= " + basket);

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