package com.example.demo.consumer;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeJsonConsumer {
    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listen(String messagae) throws JsonProcessingException {
        System.out.println("------------------------");
        Employee employee = objectMapper.readValue(messagae, Employee.class);
        System.out.println(employee);
    }
}