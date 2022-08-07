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