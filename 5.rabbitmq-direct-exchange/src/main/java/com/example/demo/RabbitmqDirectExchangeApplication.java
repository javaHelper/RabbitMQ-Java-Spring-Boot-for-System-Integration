package com.example.demo;

import com.example.demo.config.DirectExchangeConfig;
import com.example.demo.model.Picture;
import com.example.demo.producer.PictureProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqDirectExchangeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqDirectExchangeApplication.class, args);
	}

	@Autowired
	private PictureProducer producer;

	@Override
	public void run(String... args) throws Exception {
		final Picture picture = Picture.builder()
				.name("Shivneri-Fort")
				.size(100l)
				.type("JPG")
				.source("my-gallary")
				.build();
		producer.sendMessage(picture, DirectExchangeConfig.PICTURE_IMAGE_RK);
	}
}