package com.ribaso.checkoutservice.port.exception;

public class InvalidCheckoutIdException extends RuntimeException {
    public InvalidCheckoutIdException(String message) {
        super(message);
    }
}