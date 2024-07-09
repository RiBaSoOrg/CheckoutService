package com.ribaso.checkoutservice.port.exception;

public class CheckoutAlreadyCompletedException extends RuntimeException {
    public CheckoutAlreadyCompletedException(String message) {
        super(message);
    }
}
