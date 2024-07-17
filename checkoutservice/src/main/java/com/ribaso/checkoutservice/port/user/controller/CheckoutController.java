package com.ribaso.checkoutservice.port.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutService;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/initiate")
    @Operation(summary = "Initiate checkout", description = "Initiates a new checkout process and returns the checkout details.")
    public Checkout initiateCheckout(@Parameter(description = "The unique identifier of the checkout to complete.") String userId) {
        return checkoutService.initiateCheckout(userId);
    }

    @PostMapping("/complete/{id}")
    @Operation(summary = "Complete checkout", description = "Completes a checkout process for a given ID.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Checkout completed successfully", content = @Content(schema = @Schema(implementation = Checkout.class))),
    @ApiResponse(responseCode = "404", description = "Invalid Checkout ID: Provided ID does not exist", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "400", description = "Checkout is already completed for ID: Provided ID has already been completed", content = @Content(schema = @Schema(implementation = String.class))),
    @ApiResponse(responseCode = "500", description = "An unexpected error occurred", content = @Content(schema = @Schema(implementation = String.class)))
})
    public ResponseEntity<?> completeCheckout(@PathVariable  @Parameter(description = "The unique identifier of the checkout to complete.") Long id) {
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