package com.example.demo.consumer;

import com.example.demo.model.Picture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PictureVectorConsumer {

	private ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "q.picture.vector")
	public void listen(String message) throws JsonProcessingException {
		Picture p = objectMapper.readValue(message, Picture.class);
		// process the picture
		System.out.println("Picture Vector :: " + p);
	}
}