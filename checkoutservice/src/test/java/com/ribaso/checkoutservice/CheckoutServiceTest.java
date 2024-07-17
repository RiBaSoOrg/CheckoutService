package com.ribaso.checkoutservice;

import com.ribaso.checkoutservice.core.domain.model.Basket;
import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.core.domain.model.Item;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutService;
import com.ribaso.checkoutservice.port.basket.GetBasket;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;
import com.ribaso.checkoutservice.core.domain.service.impl.CheckoutServiceImpl;
import com.ribaso.checkoutservice.core.domain.service.interfaces.CheckoutRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

    @Mock
    private CheckoutRepository checkoutRepository;

    @Mock
    private GetBasket getBasket;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;
    
    @Mock
    private RabbitTemplate rabbitTemplate;
    
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


     @Test
    public void initiateCheckout_ShouldCreatePendingCheckout() throws Exception {
        // Arrange
        Checkout pendingCheckout = new Checkout();
        pendingCheckout.setStatus("pending");

        Basket basket = new Basket();
        basket.setUserId("1");

        Item item = new Item();
        item.setItemId(1L);
        item.setQuantity(2);
        item.setBasket(basket);

        basket.setItems(Collections.singletonList(item));

        // Jackson2JsonMessageConverter for the test
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(converter);

        // Simulate Basket as JSON
        Message responseMessage = converter.toMessage(basket, new MessageProperties());

        // Configure the mock RabbitTemplate to return the simulated response
        when(rabbitTemplate.convertSendAndReceive(eq("exchange"), eq("basketRoutingKey"), any(String.class)))
                .thenReturn(responseMessage);

        when(checkoutRepository.save(any(Checkout.class))).thenReturn(pendingCheckout);

        // Act
        Checkout createdCheckout = checkoutService.initiateCheckout("1");

        // Assert
        assertNotNull(createdCheckout);
        assertEquals("pending", createdCheckout.getStatus());
        assertNotNull(createdCheckout.getBasket());
        assertEquals(1L, createdCheckout.getBasket().getUserId());
        assertEquals(1, createdCheckout.getBasket().getItems().size());
        assertEquals(1L, createdCheckout.getBasket().getItems().get(0).getItemId());
        assertEquals(2, createdCheckout.getBasket().getItems().get(0).getQuantity());
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