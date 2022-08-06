package com.example.demo;

import com.example.demo.model.Employee;
import com.example.demo.producer.EmployeeJsonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class ProducerConsumerWithJsonApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProducerConsumerWithJsonApplication.class, args);
	}

	@Autowired
	private EmployeeJsonProducer producer;

	@Override
	public void run(String... args) throws Exception {
		Employee employee = Employee.builder()
				.employeeId(1L)
				.name("John Doe")
				.birthDate(new Date())
				.build();

		producer.sendMessage(employee);
	}
}