package com.example.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class SimpleConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE, concurrency = "3")
    public void listen(String message) throws InterruptedException {
        System.out.println("--------------------");

        // simulate long process, up to 2 seconds
        Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        System.out.println("Consuming : "+message);
    }
}