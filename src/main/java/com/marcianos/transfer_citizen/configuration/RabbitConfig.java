package com.marcianos.transfer_citizen.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }


    @Bean
    public TopicExchange transferCitizenExchange() {
        return new TopicExchange("transfer_citizen_exchange");
    }

    @Bean
    public Queue transferCitizenQueue() {
        return new Queue("transfer_citizen_queue");
    }

    @Bean
    public Binding binding(TopicExchange transferCitizenExchange, Queue transferCitizenQueue) {
        return BindingBuilder.bind(transferCitizenQueue).to(transferCitizenExchange).with("transfer_citizen_routing_key");
    }

}
