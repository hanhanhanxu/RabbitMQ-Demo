package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: HanXu
 * on 2024/6/27
 * Class description: 测试自动创建的交换机、队列 消息发送情况
 */
@SpringBootTest
public class MessageAutoCreateSenderTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试手动创建的交换机、队列
     */
    @Test
    void testTopic() {
        String exchange = "code.topic.exchange";
//        String routingKey = "shanghai.new";
//        String routingKey = "shanghai.weather";
        String routingKey = "beijing.new";
        rabbitTemplate.convertAndSend(exchange, routingKey,  "hello Topic Exchange!" + routingKey);
    }

    /**
     * 测试自动创建的交换机、队列
     */
    @Test
    void testTopicAnnotation() {
        String exchange = "annotation.topic.exchange";
//        String routingKey = "shanghai.new";
//        String routingKey = "shanghai.weather";
        String routingKey = "beijing.new";
        rabbitTemplate.convertAndSend(exchange, routingKey,  "hello Annotation Topic Exchange!" + routingKey);
    }

    /**
     * 测试发送对象消息
     */
    @Test
    void testSendObject() {
        String exchange = "annotation.topic.exchange.obj";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Jack");
        map.put("age", 20);
        rabbitTemplate.convertAndSend(exchange, "obj.map",  map);
    }

}
