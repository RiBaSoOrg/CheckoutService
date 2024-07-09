package com.ribaso.checkoutservice.port.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutService;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/initiate")
    public Checkout initiateCheckout() {
        return checkoutService.initiateCheckout();
    }

    @PostMapping("/complete/{id}")
    public ResponseEntity<?> completeCheckout(@PathVariable Long id) {
        try {
            Checkout checkout = checkoutService.completeCheckout(id);
            return ResponseEntity.ok(checkout);
        } catch (InvalidCheckoutIdException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Checkout ID: " + id);
        } catch (CheckoutAlreadyCompletedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Checkout is already completed for ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}