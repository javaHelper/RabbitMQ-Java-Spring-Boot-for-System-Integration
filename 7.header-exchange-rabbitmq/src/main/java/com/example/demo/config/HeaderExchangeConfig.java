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