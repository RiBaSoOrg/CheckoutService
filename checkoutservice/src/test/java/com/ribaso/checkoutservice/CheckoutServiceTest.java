package com.ribaso.checkoutservice;

import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutService;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;
import com.ribaso.checkoutservice.core.domain.service.impl.CheckoutServiceImpl;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

    @Mock
    private CheckoutRepository checkoutRepository;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    public void initiateCheckout_ShouldCreatePendingCheckout() {
        // Arrange
        Checkout pendingCheckout = new Checkout();
        pendingCheckout.setStatus("pending");
        when(checkoutRepository.save(any(Checkout.class))).thenReturn(pendingCheckout);

        // Act
        Checkout createdCheckout = checkoutService.initiateCheckout();

        // Assert
        assertNotNull(createdCheckout);
        assertEquals("pending", createdCheckout.getStatus());
    }

    @Test
    public void completeCheckout_InvalidId_ShouldThrowInvalidCheckoutIdException() {
        // Arrange
        when(checkoutRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(InvalidCheckoutIdException.class, () -> {
            checkoutService.completeCheckout(1L);
        });
    }

    @Test
    public void completeCheckout_AlreadyCompleted_ShouldThrowCheckoutAlreadyCompletedException() {
        // Arrange
        Checkout completedCheckout = new Checkout();
        completedCheckout.setStatus("completed");
        when(checkoutRepository.findById(anyLong())).thenReturn(java.util.Optional.of(completedCheckout));

        // Act & Assert
        assertThrows(CheckoutAlreadyCompletedException.class, () -> {
            checkoutService.completeCheckout(1L);
        });
    }

    @Test
    public void completeCheckout_ValidRequest_ShouldCompleteCheckout() {
        // Arrange
        Checkout pendingCheckout = new Checkout();
        pendingCheckout.setStatus("pending");
        when(checkoutRepository.findById(anyLong())).thenReturn(java.util.Optional.of(pendingCheckout));
        when(checkoutRepository.save(any(Checkout.class))).thenReturn(pendingCheckout);

        // Act
        Checkout completedCheckout = checkoutService.completeCheckout(1L);

        // Assert
        assertNotNull(completedCheckout);
        assertEquals("completed", completedCheckout.getStatus());
    }
}