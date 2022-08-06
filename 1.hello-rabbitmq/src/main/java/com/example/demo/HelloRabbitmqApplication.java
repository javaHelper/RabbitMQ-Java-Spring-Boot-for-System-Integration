package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloRabbitmqApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HelloRabbitmqApplication.class, args);
	}

	@Autowired
	private SimpleProducer simpleProducer;

	@Override
	public void run(String... args) throws Exception {
		simpleProducer.sendHello("Prateek - How are you?");
	}
}