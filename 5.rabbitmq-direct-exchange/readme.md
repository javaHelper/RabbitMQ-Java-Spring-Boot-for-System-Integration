# Exchange Type: Direct

> Here, the routing of the message takes place where the binding key is exactly matched with the binding Queue, the message will be sent to the queue which consist of the exact match. It is also known as one-to-one exchange.

- Send to selective queues
- Based on routing key
- Message can be discarded

# Coding example
- Create queues: q.picture.image and q.picture.vector
- Create an exchange: x.picture
- Create bindings:
     - x.picture, routing key = jpg => q.picture.image
     - x.picture, routing key = svg => q.picture.vector
     

<img width="534" alt="Screenshot 2022-08-07 at 1 25 37 PM" src="https://user-images.githubusercontent.com/54174687/183281282-4fd184ab-3261-4904-aa71-4a8d94e24bbe.png">


<img width="1347" alt="Screenshot 2022-08-07 at 1 26 38 PM" src="https://user-images.githubusercontent.com/54174687/183281257-51895533-58f3-4b5d-a7de-c943f5bebbcf.png">

<img width="1004" alt="Screenshot 2022-08-07 at 1 26 15 PM" src="https://user-images.githubusercontent.com/54174687/183281273-37e117d7-0727-49a2-ba7d-e0d9f13b2220.png">

<img width="613" alt="Screenshot 2022-08-07 at 1 25 57 PM" src="https://user-images.githubusercontent.com/54174687/183281270-c1702ea3-52ba-4599-9046-bc4fa4d21cbe.png">

- DirectExchangeConfig.java

```java
package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {
    public static final String PICTURE_IMAGE_QUEUE = "q.picture.image";
    public static final String PICTURE_VECTOR_QUEUE = "q.picture.vector";
    public static final String EXCHANGE = "x.picture";

    public static final String PICTURE_VECTOR_RK = "picture-vector-rk";
    public static final String PICTURE_IMAGE_RK = "picture-image-rk";

    @Bean
    public Queue pictureImageQueue(){
        return new Queue(PICTURE_IMAGE_QUEUE, false);
    }

    @Bean
    public Queue pictureVectorQueue(){
        return new Queue(PICTURE_VECTOR_QUEUE, false);
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding pictureImageBinding(DirectExchange exchange) {
        return BindingBuilder.bind(pictureImageQueue())
                .to(exchange)
                .with(PICTURE_IMAGE_RK);
    }

    @Bean
    public Binding pictureVectorBinding(DirectExchange exchange){
        return BindingBuilder.bind(pictureVectorQueue())
                .to(exchange)
                .with(PICTURE_VECTOR_RK);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
```

- PictureProducer.java

```java
@Service
public class PictureProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessage(Picture p, String routingKey) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(p);
        rabbitTemplate.convertAndSend("x.picture", routingKey, json);
    }
}
```

- MainApp.java

```java
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
```

- Picture.java

```java
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Picture {
    private String name;
    private String type;
    private String source;
    private long size;
}
```

- PictureImageConsumer.java

```java
@Service
public class PictureImageConsumer {

	private ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "q.picture.image")
	public void listen(String message) throws JsonProcessingException {
		System.out.println("===========================");
		Picture p = objectMapper.readValue(message, Picture.class);
		// process the image
		System.out.println("Creating thumbnail & publishing : " + p);
	}
}
```

- PictureVectorConsumer.java

```java
@Service
public class PictureVectorConsumer {

	private ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "q.picture.vector")
	public void listen(String message) throws JsonProcessingException {
		System.out.println("-------------------------");
		Picture p = objectMapper.readValue(message, Picture.class);
		// process the picture
		System.out.println("Convert to image, creating thumbnail, & publishing : " + p);
	}
}
```
