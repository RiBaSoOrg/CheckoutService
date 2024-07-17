package com.ribaso.checkoutservice.port.exception;

public class UnknownUserIDException extends RuntimeException {
    public UnknownUserIDException(String message) {
        super(message);
    }
}