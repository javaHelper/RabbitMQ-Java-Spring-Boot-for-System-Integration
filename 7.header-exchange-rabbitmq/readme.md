#

<img width="617" alt="Screenshot 2022-08-07 at 4 05 24 PM" src="https://user-images.githubusercontent.com/54174687/183286834-c49113e1-60e3-4e47-a0fe-228b54cdd2de.png">

<img width="651" alt="Screenshot 2022-08-07 at 4 05 54 PM" src="https://user-images.githubusercontent.com/54174687/183286839-46a808e4-72e1-494a-839a-c83c03682264.png">

- HeaderExchangeConfig.java

```java
package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderExchangeConfig {
    @Bean
    public Queue marketingQueue() {
        return new Queue("marketingQueue", false);
    }

    @Bean
    public Queue financeQueue() {
        return new Queue("financeQueue", false);
    }

    @Bean
    public Queue adminQueue() {
        return new Queue("adminQueue", false);
    }

    @Bean
    public HeadersExchange headerExchange() {
        return new HeadersExchange("header-exchange");
    }

    @Bean
    public Binding marketingBinding(Queue marketingQueue, HeadersExchange headerExchange) {
        return BindingBuilder.bind(marketingQueue)
                .to(headerExchange)
                .where("department").matches("marketing");
    }

    @Bean
    public Binding financeBinding(Queue financeQueue, HeadersExchange headerExchange) {
        return BindingBuilder.bind(financeQueue)
                .to(headerExchange)
                .where("department").matches("finance");
    }

    @Bean
    public Binding adminBinding(Queue adminQueue, HeadersExchange headerExchange) {
        return BindingBuilder.bind(adminQueue)
                .to(headerExchange)
                .where("department").matches("admin");
    }
}
```

- AdminConsumer.java - you can create other consumers

```java
@Service
public class AdminConsumer {

    @RabbitListener(queues = "adminQueue")
    public void listen(String message){
        System.out.println("Admin Consumer read :"+ message);
    }
}
```

- MessageProducer.java

```java
@Configuration
public class MessageProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String messageData, MessageProperties messageProperties){
        MessageConverter messageConverter = new SimpleMessageConverter();
        Message message = messageConverter.toMessage(messageData, messageProperties);

        amqpTemplate.send("header-exchange", "", message);
    }
}
```

MainApp.java

```java
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
```
