package com.ribaso.checkoutservice.port.basket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ribaso.checkoutservice.core.domain.model.Basket;
import com.ribaso.checkoutservice.port.exception.UnknownUserIDException;



@Service
public class GetBasket {

    private static final Logger log = LoggerFactory.getLogger(Basket.class);
    
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public GetBasket(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Basket getBasket(String basketId) {
    log.info("Sending basketId: {}", basketId);
    Basket response =  (Basket) rabbitTemplate.convertSendAndReceive("exchange", "basketRoutingKey", basketId);
    log.info(basketId, response);
    if (response == null) {
        throw new UnknownUserIDException("BasketId not found for ID: " + basketId);
    }
    return response;
}

}
