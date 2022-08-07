package com.example.demo.consumer;

import com.example.demo.model.Picture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PictureFilterConsumer {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "q.picture.filter")
	public void listen(String message) throws JsonProcessingException {
		Picture p = objectMapper.readValue(message, Picture.class);
		// process the image
		System.out.println("Applying image filter : " + p);
	}
}