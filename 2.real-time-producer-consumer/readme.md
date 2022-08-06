# Realtime Producer and consumer

- RabbitMQConfig.java

```java
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE = "spring-boot";
    public static final String EXCHANGE = "spring-boot-exchange";
    public static final String ROUTING_KEY = "spring-boot-routingKey";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE, false);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
```

- SimpleProducer.java

```java

@Service
public class SimpleProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private int count = 0;

    /**  Sending a message every 500 ms  **/
    @Scheduled(fixedRate = 500)
    public void sendHello(){
        count++;
        System.out.println("Count is : "+count);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY, "Hello : "+count);
    }
}
```

# Multiple Consumers for Each Queue

- Publisher works faster than consumer.
- Consumer bottleneck.
- Solution? Use multiple consumers.

- SimpleConsumer.java

```java
@Service
public class SimpleConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE, concurrency = "3")
    public void listen(String message) throws InterruptedException {
        System.out.println("--------------------");

        // simulate long process, up to 2 seconds
        Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        System.out.println("Consuming : "+message);
    }
}
```

- <img width="865" alt="Screenshot 2022-08-06 at 11 50 27 PM" src="https://user-images.githubusercontent.com/54174687/183261304-521a2a57-a55c-4abe-a88a-97de5fcb3433.png">

<img width="1022" alt="Screenshot 2022-08-06 at 11 45 52 PM" src="https://user-images.githubusercontent.com/54174687/183261302-97ae58a4-4416-4a39-ab66-ec825bafb175.png">
