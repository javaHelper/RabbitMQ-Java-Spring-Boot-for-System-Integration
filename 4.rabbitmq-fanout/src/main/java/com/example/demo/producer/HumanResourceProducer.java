package com.example.demo.producer;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanResourceProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

	public void sendMessage(Employee e) throws JsonProcessingException {
		String json = objectMapper.writeValueAsString(e);
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,"", json);
        System.out.println("Message Sent");
	}
}