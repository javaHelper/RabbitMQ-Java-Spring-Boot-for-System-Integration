package com.example.demo;

import com.example.demo.producer.MessageProducer;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HeaderExchangeRabbitmqApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HeaderExchangeRabbitmqApplication.class, args);
	}

	@Autowired
	private MessageProducer producer;

	@Override
	public void run(String... args) throws Exception {
		// Note - you can set department values like admin, marketing and finance
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("department","admin");

		producer.sendMessage("Hello - How are you?", messageProperties);
	}
}