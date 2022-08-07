package com.example.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AdminConsumer {

    @RabbitListener(queues = "adminQueue")
    public void listen(String message){
        System.out.println("Admin Consumer read :"+ message);
    }
}