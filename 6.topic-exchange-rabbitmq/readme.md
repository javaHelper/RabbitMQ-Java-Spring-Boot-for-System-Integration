# Topic Exchange
The logic behind the topic-exchange is similar to that of direct exchange. The only difference is here, there is no need to have an exact matching. The pattern used for routing can be in the form of Regular expression i.e. we can use symbols like dot(.), asterisk(*) or Hash(#).

- Multiple criteria routing
- Two special characters on routing key

# Coding example
- Create queues: q.picture.filter, q.picture.log
- Create exchange : x.picture2 ,type: topic

# Create bindings:
- q.picture.image, routing key: *.*.png
- q.picture.image, routing key: #.jpg
- q.picture.vector, routing key: *.*.svg
- q.picture.filter, routing key: mobile.#
- q.picture.log, routing key: *.large.svg

<img width="541" alt="Screenshot 2022-08-07 at 3 04 44 PM" src="https://user-images.githubusercontent.com/54174687/183284676-ce16dd4d-992f-44c6-986b-3ce8c9ced7dc.png">
<img width="619" alt="Screenshot 2022-08-07 at 3 04 56 PM" src="https://user-images.githubusercontent.com/54174687/183284682-b642931b-1707-44bb-acb4-5b23b1bec7d6.png">
<img width="525" alt="Screenshot 2022-08-07 at 3 05 11 PM" src="https://user-images.githubusercontent.com/54174687/183284683-10b8a90f-c426-4d20-b31c-a1902b08d52c.png">
<img width="869" alt="Screenshot 2022-08-07 at 3 05 22 PM" src="https://user-images.githubusercontent.com/54174687/183284686-3088c534-6f4c-4ccd-bba9-acbf7ef217be.png">

- TopicConfig.java

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
public class TopicConfiguration {
    @Bean
    public Queue pictureImageQueue(){
        return new Queue("q.picture.image", false);
    }

    @Bean
    public Queue pictureVectorQueue(){
        return new Queue("q.picture.vector", false);
    }

    @Bean
    public Queue pictureFilterQueue(){
        return new Queue("q.picture.filter", false);
    }

    @Bean
    public Queue pictureLogQueue(){
        return new Queue("q.picture.log", false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("x.picture2");
    }

    @Bean
    public Binding pictureImageBinding1(TopicExchange exchange) {
        return BindingBuilder.bind(pictureImageQueue())
                .to(exchange)
                .with("*.*.png");
    }

    @Bean
    public Binding pictureImageBinding2(TopicExchange exchange) {
        return BindingBuilder.bind(pictureImageQueue())
                .to(exchange)
                .with("#.jpg");
    }

    @Bean
    public Binding pictureVectorBinding(TopicExchange exchange){
        return BindingBuilder.bind(pictureVectorQueue())
                .to(exchange)
                .with("*.*.svg");
    }

    @Bean
    public Binding pictureFilterBinding(TopicExchange exchange){
        return BindingBuilder.bind(pictureFilterQueue())
                .to(exchange)
                .with("mobile.#");
    }

    @Bean
    public Binding pictureLogBinding(TopicExchange exchange){
        return BindingBuilder.bind(pictureLogQueue())
                .to(exchange)
                .with("*.large.svg");
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

- PictureProducer2.java

```java
@Service
public class PictureProducer2 {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessage(Picture p) throws JsonProcessingException {
        StringBuilder sb = new StringBuilder();
        // 1st word is �mobile� or �web�, which is picture source
        sb.append(p.getSource());
        sb.append('.');

        // 2nd word is �large� or �small�, which is based on picture size. Picture with
        // size more than 4000 are considered �large�
        if (p.getSize() > 4000) {
            sb.append("large");
        } else {
            sb.append("small");
        }
        sb.append('.');

        // 3rd word is picture type (jpg, png, or svg)
        sb.append(p.getType());

        String routingKey = sb.toString();
        System.out.println("RoutingKey : "+ routingKey);

        String json = objectMapper.writeValueAsString(p);
        rabbitTemplate.convertAndSend("x.picture2", routingKey, json);
    }
}
```

- PictureFilterProducer.java

```java
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
```

- PictureImageConsumer.java

```java
@Service
public class PictureImageConsumer {

	private ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "q.picture.image")
	public void listen(String message) throws JsonProcessingException {
		Picture p = objectMapper.readValue(message, Picture.class);
		// process the image
		System.out.println("Picture Image : " + p);
	}
}
```

- PictureLogConsumer.java

```java
@Service
public class PictureLogConsumer {

	private ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "q.picture.log")
	public void listen(String message) throws JsonProcessingException {
		Picture p = objectMapper.readValue(message, Picture.class);
		// process the image
		System.out.println("Logging Picture Log => " + p);
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
		Picture p = objectMapper.readValue(message, Picture.class);
		// process the picture
		System.out.println("Picture Vector :: " + p);
	}
}
```

- MainApp.java

```java
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
```
