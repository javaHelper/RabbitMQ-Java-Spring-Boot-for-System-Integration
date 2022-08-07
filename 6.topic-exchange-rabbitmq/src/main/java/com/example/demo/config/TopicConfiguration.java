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