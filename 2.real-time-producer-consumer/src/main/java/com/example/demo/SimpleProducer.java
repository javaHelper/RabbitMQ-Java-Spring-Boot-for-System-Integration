package com.example.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SimpleProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private int count = 0;

    /**  Sending a message every 500 ms  **/
    @Scheduled(fixedRate = 500)
    public void sendHello(){
        count++;
        System.out.println("Count is : "+count);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY, "Hello : "+count);
    }
}