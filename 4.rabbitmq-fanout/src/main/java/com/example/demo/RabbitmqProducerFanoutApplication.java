package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.producer.HumanResourceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class RabbitmqProducerFanoutApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqProducerFanoutApplication.class, args);
	}

	@Autowired
	private HumanResourceProducer producer;

	@Override
	public void run(String... args) throws Exception {
		Employee e = Employee.builder().employeeId(1L).birthDate(new Date()).name("Jane Doe").build();
		producer.sendMessage(e);
	}
}