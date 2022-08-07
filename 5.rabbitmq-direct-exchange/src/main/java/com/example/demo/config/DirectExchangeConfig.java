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