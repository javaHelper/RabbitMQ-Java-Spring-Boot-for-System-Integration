# Fanout 


# Fanout Exchange
> The Fanout Exchange is used where the message needs to be passed to all the queues bounded it. It is one-to-many type of Exchange. In case of fanout-exchange, routing key is not needed.

<img width="509" alt="Screenshot 2022-08-07 at 11 58 56 AM" src="https://user-images.githubusercontent.com/54174687/183278268-6af79c86-616a-418e-af90-475f1d5417e8.png">

<img width="1362" alt="Screenshot 2022-08-07 at 11 50 24 AM" src="https://user-images.githubusercontent.com/54174687/183278316-d684757d-e45c-4e12-bef1-1d8590542caa.png">

<img width="664" alt="Screenshot 2022-08-07 at 11 50 36 AM" src="https://user-images.githubusercontent.com/54174687/183278319-280fe09d-e11a-4638-9036-77cfe8532f02.png">

<img width="905" alt="Screenshot 2022-08-07 at 11 50 45 AM" src="https://user-images.githubusercontent.com/54174687/183278341-407a01cc-7205-4f2c-bf42-fe4e0c41b388.png">

- FanoutConfiguration.java

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
public class RabbitMQConfig {
    public static final String HR_ACCOUNT_QUEUE = "q.hr.accounting";
    public static final String HR_MARKETING_QUEUE = "q.hr.marketing";
    public static final String EXCHANGE = "x.hr";

    public static final String HR_ACCT_ROUTING_KEY = "hr-accounting-routingKey";
    public static final String HR_MRKT_ROUTING_KEY = "hr-marketing-routingKey";

    @Bean
    public Queue hrAccountQueue(){
        return new Queue(HR_ACCOUNT_QUEUE, false);
    }

    @Bean
    public Queue hrMarketingQueue(){
        return new Queue(HR_MARKETING_QUEUE, false);
    }

    @Bean
    public FanoutExchange exchange(){
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    public Binding hrAccountBinding(FanoutExchange exchange) {
        return BindingBuilder.bind(hrAccountQueue())
                .to(exchange);
    }

    @Bean
    public Binding hrMarketingBinding(FanoutExchange exchange){
        return BindingBuilder.bind(hrMarketingQueue())
                .to(exchange);
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

- HumanResourceProducer.java

```java
import com.example.demo.config.RabbitMQConfig;
import com.example.demo.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanResourceProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

	public void sendMessage(Employee e) throws JsonProcessingException {
		String json = objectMapper.writeValueAsString(e);
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,"", json);
        System.out.println("Message Sent");
	}
}
```

- AccountingConsumer.java

```java
@Service
public class AccountingConsumer {
    private ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitMQConfig.HR_ACCOUNT_QUEUE)
    public void listen(String message) throws JsonProcessingException {
        Employee e = objectMapper.readValue(message, Employee.class);
        System.out.println("### On accounting : " + e);
    }
}
```

- MarketingConsumer.java

```java
@Service
public class MarketingConsumer {

	private ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = RabbitMQConfig.HR_MARKETING_QUEUE)
	public void listen(String message) throws JsonProcessingException {
		Employee e = objectMapper.readValue(message, Employee.class);
		System.out.println("### On marketing : " + e);
	}
}
```

- Employee.java

```java
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Employee {

    @JsonProperty("employee_id")
    private Long employeeId;

    private String name;

    @JsonProperty("birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "Asia/Jakarta")
    private Date birthDate;
}
```
