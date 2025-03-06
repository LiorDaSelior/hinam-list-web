package com.hinamlist.hinam_list_web.service;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitConfig {
    @Bean
    @Qualifier("${rabbitmq.algorithm-producer.exchange}")
    DirectExchange AlgorithmProducerExchange(@Value("${rabbitmq.algorithm-producer.exchange}")  String collectorExchangeName) {
        return new DirectExchange(collectorExchangeName);
    }

    @Bean
    @Qualifier("${rabbitmq.algorithm-producer.queue}")
    Queue AlgorithmProducerQueue(@Value("${rabbitmq.algorithm-producer.queue}")  String collectorQueueName,
                                 RabbitAdmin admin) {
/*        try {
            admin.getQueueProperties(collectorQueueName);
            admin.purgeQueue(collectorQueueName);
        }
        catch (AmqpConnectException _) {

        }*/
        return new Queue(collectorQueueName);
    }

    @Bean
    public Binding AlgorithmProducerBinding(@Qualifier("${rabbitmq.algorithm-producer.exchange}")DirectExchange collectorExchange,
                                               @Qualifier("${rabbitmq.algorithm-producer.queue}")Queue collectorQueue) {
        return BindingBuilder.bind(collectorQueue).to(collectorExchange).with("");
    }

    @Bean
    @Qualifier("${rabbitmq.algorithm-consumer.exchange}")
    DirectExchange AlgorithmConsumerExchange(@Value("${rabbitmq.algorithm-consumer.exchange}")  String collectorExchangeName) {
        return new DirectExchange(collectorExchangeName);
    }

    @Bean
    @Qualifier("${rabbitmq.algorithm-consumer.queue}")
    Queue AlgorithmConsumerQueue(@Value("${rabbitmq.algorithm-consumer.queue}")  String collectorQueueName,
                                 RabbitAdmin admin) {
/*        try {
            admin.getQueueProperties(collectorQueueName);
            admin.purgeQueue(collectorQueueName);
        }
        catch (AmqpConnectException _) {

        }*/
        return new Queue(collectorQueueName);
    }

    @Bean
    public Binding AlgorithmConsumerBinding(@Qualifier("${rabbitmq.algorithm-consumer.exchange}")DirectExchange collectorExchange,
                                            @Qualifier("${rabbitmq.algorithm-consumer.queue}")Queue collectorQueue) {
        return BindingBuilder.bind(collectorQueue).to(collectorExchange).with("");
    }

    @Bean
    @Qualifier("${rabbitmq.converter.exchange}")
    DirectExchange ConverterExchange(@Value("${rabbitmq.converter.exchange}")  String collectorExchangeName) {
        return new DirectExchange(collectorExchangeName);
    }

    @Bean
    @Qualifier("${rabbitmq.converter.queue}")
    Queue ConverterQueue(@Value("${rabbitmq.converter.queue}")  String collectorQueueName,
                         RabbitAdmin admin) {
/*        try {
            admin.getQueueProperties(collectorQueueName);
            admin.purgeQueue(collectorQueueName);
        }
        catch (AmqpConnectException _) {

        }*/
        return new Queue(collectorQueueName);
    }

    @Bean
    public Binding ConverterBinding(@Qualifier("${rabbitmq.converter.exchange}")DirectExchange collectorExchange,
                                            @Qualifier("${rabbitmq.converter.queue}")Queue collectorQueue) {
        return BindingBuilder.bind(collectorQueue).to(collectorExchange).with("");
    }

    @Bean("trustedConverter")
    public SimpleMessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("com.hinamlist.hinam_list_web.model.*", "java.util.*"));
        return converter;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        return admin;
    }
}
