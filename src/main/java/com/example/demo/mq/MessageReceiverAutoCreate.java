package com.example.demo.mq;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

import java.util.Map;

/**
 * @author: HanXu
 * on 2024/6/27
 * Class description: 测试自动创建的队列 交换机 @MQConfig
 */
//@Component
public class MessageReceiverAutoCreate {

    @RabbitListener (queues = "code.topic.queue")
    public void receiverTopic(String message) {
        System.out.println("Received code.topic.queue <" + message + "> 消费者");
    }

    @RabbitListener (queues = "code.topic.queue.beijing")
    public void receiverTopicBeiJing(String message) {
        System.out.println("Received code.topic.queue.beijing <" + message + "> 消费者");
    }


    /**
     * 创建队列 annotation.topic.queue ,创建交换机 annotation.topic.exchange ,并将队列绑定到交换机上，指定的routingKey是shanghai.#
     * 创建队列 annotation.topic.queue2 ,并将队列绑定到交换机上，指定的routingKey是shanghai.#
     * 该消费者从两个队列中接收消息
     * @param message
     */
    @RabbitListener (bindings = {
            @QueueBinding(
                    value =  @Queue(name = "annotation.topic.queue", durable = "true"),
                    exchange = @Exchange(name = "annotation.topic.exchange", type = ExchangeTypes.TOPIC),
                    key = {"shanghai.#"}
            ),
            @QueueBinding(
                    value =  @Queue(name = "annotation.topic.queue2", durable = "true"),
                    exchange = @Exchange(name = "annotation.topic.exchange", type = ExchangeTypes.TOPIC),
                    key = {"beijing.#"}
            ),
    })
    public void receiverTopicAnnotation(String message) {
        System.out.println("Received annotation.topic.queue <" + message + "> 消费者");
    }


    /**
     * 测试接收对象消息
     * @param map
     */
    @RabbitListener (bindings = {
            @QueueBinding(
                    value =  @Queue(name = "annotation.topic.queue.obj", durable = "true"),
                    exchange = @Exchange(name = "annotation.topic.exchange.obj", type = ExchangeTypes.TOPIC),
                    key = {"obj.#"}
            ),
    })
    public void receiverTopicAnnotationObj(Map map) {
        System.out.println("Received annotation.topic.queue.obj <" + map + "> 消费者");
    }
}
