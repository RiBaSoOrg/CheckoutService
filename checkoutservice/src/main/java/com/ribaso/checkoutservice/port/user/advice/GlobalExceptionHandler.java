package com.ribaso.checkoutservice.port.user.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCheckoutIdException.class)
    public ResponseEntity<String> handleInvalidCheckoutIdException(InvalidCheckoutIdException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CheckoutAlreadyCompletedException.class)
    public ResponseEntity<String> handleCheckoutAlreadyCompletedException(CheckoutAlreadyCompletedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
