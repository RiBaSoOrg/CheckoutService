package com.ribaso.checkoutservice;
import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutService;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;
import com.ribaso.checkoutservice.port.user.controller.CheckoutController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(CheckoutController.class)
public class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutService checkoutService;

    @Test
    public void initiateCheckout_ShouldReturnCheckout() throws Exception {
        Checkout checkout = new Checkout();
        checkout.setStatus("pending");
        given(checkoutService.initiateCheckout()).willReturn(checkout);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/initiate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"pending\"}"));
    }

    @Test
    public void completeCheckout_ValidId_ShouldReturnUpdatedCheckout() throws Exception {
        Checkout checkout = new Checkout();
        checkout.setStatus("completed");
        given(checkoutService.completeCheckout(1L)).willReturn(checkout);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/complete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"completed\"}"));
    }

    @Test
    public void completeCheckout_InvalidId_ShouldReturnNotFound() throws Exception {
        given(checkoutService.completeCheckout(1L)).willThrow(new InvalidCheckoutIdException("Invalid Checkout ID"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/complete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void completeCheckout_AlreadyCompleted_ShouldReturnBadRequest() throws Exception {
        // Arrange
        given(checkoutService.completeCheckout(1L)).willThrow(new CheckoutAlreadyCompletedException("Checkout is already completed"));
    
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/complete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Checkout is already completed")));
    }
    
}