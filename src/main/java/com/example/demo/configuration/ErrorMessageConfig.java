package com.example.demo.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: HanXu
 * on 2024/7/2
 * Class description: 消息消费失败重试耗尽后的处理策略：交由指定的交换机队列
 */
@Configuration
public class ErrorMessageConfig {

    public static final String exchangeName = "error.exchange";
    public static final String queueName = "error.queue";
    public static final String routingKey = "error";

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(queueName);
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorExchange) {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with(routingKey);
    }

    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, exchangeName, routingKey);
    }
}
