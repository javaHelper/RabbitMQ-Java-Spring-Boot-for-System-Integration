package com.example.demo.consumer;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AccountingConsumer {
    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitMQConfig.HR_ACCOUNT_QUEUE)
    public void listen(String message) throws JsonProcessingException {
        Employee e = objectMapper.readValue(message, Employee.class);
        System.out.println("### On accounting : " + e);
    }
}