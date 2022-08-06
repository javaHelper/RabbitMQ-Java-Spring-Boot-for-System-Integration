package com.example.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SimpleConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listen(String message){
        System.out.println("--------------------");
        System.out.println("Consuming : "+message);
    }
}