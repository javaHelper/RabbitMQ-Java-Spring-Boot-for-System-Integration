package com.example.demo;

import com.example.demo.model.Picture;
import com.example.demo.producer.PictureProducer2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TopicExchangeRabbitmqApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TopicExchangeRabbitmqApplication.class, args);
	}

	@Autowired
	private PictureProducer2 pictureProducer2;

	@Override
	public void run(String... args) throws Exception {
		final Picture picture = Picture.builder()
				.type("jpg")
				.size(4000)
				.name("Hundai I20")
				.source("mobile")
				.build();

		final Picture picture2 = Picture.builder()
				.type("jpg")
				.size(8000)
				.name("TATA NEXON")
				.source("web")
				.build();

		final Picture picture3 = Picture.builder()
				.type("svg")
				.size(8000)
				.name("TATA NEXON")
				.source("web")
				.build();

		pictureProducer2.sendMessage(picture3);
	}
}