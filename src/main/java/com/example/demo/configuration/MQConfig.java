package com.example.demo.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * @author: HanXu
 * on 2024/6/27
 * Class description: 代码自动创建队列、交换机，并绑定
 * 一般都是在消费者端创建的，因为生产者只管向交换机投递消息，不关心这些。
 */
//@Configuration
public class MQConfig {


    /**
     * 声明一个交换机，topic模式
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return ExchangeBuilder.topicExchange("code.topic.exchange").build();
//        return new TopicExchange("code.topic.exchange");
    }

    /**
     * 声明一个队列
     * @return
     */
    @Bean
    public Queue topicQueue() {
        return QueueBuilder.durable("code.topic.queue").build();
//        return new Queue("code.topic.queue");
    }

    /**
     * 声明一个队列
     * @return
     */
    @Bean
    public Queue topicQueueBeiJing() {
        return QueueBuilder.durable("code.topic.queue.beijing").build();
//        return new Queue("code.topic.queue.beijing");
    }


    /**
     * 将队列 code.topic.queue 绑定到交换机 code.topic.exchange 上，并指定routingKey为 shanghai.#
     * @param topicQueue
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding topicBinding(Queue topicQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("shanghai.#");
    }

    /**
     * 将队列 code.topic.queue.beijing 绑定到交换机 code.topic.exchange 上，并指定routingKey为 beijing.#
     * @param topicQueueBeiJing
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding topicBinding2(Queue topicQueueBeiJing, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueBeiJing).to(topicExchange).with("beijing.#");
    }
}
