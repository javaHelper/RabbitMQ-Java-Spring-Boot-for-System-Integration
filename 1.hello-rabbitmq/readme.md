# Hello World - Producer and Consumer example

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

- Producer.java

```java
@Service
public class SimpleProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendHello(String name){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY, "Hello : "+name);
    }
}
```

- Consumer.java

```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SimpleConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listen(String message){
        System.out.println("--------------------");
        System.out.println("Consuming : "+message);
    }
}
```
<img width="1362" alt="Screenshot 2022-08-06 at 11 37 11 PM" src="https://user-images.githubusercontent.com/54174687/183260793-f3b30efc-93b7-4076-b0b2-f8ea99757276.png">


<img width="1008" alt="Screenshot 2022-08-06 at 11 34 33 PM" src="https://user-images.githubusercontent.com/54174687/183260721-2151a5ec-f9ab-4c1f-bccc-65ba5ba9f523.png">

<img width="685" alt="Screenshot 2022-08-06 at 11 34 08 PM" src="https://user-images.githubusercontent.com/54174687/183260716-8502b3df-5aa8-49fa-8941-984a04c6812f.png">
