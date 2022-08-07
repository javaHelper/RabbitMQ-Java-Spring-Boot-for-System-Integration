package com.example.demo.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MessageProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String messageData, MessageProperties messageProperties){
        MessageConverter messageConverter = new SimpleMessageConverter();
        Message message = messageConverter.toMessage(messageData, messageProperties);

        amqpTemplate.send("header-exchange", "", message);
    }
}