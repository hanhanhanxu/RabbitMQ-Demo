package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageSenderTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //------------------------------------------simple 一对一---------------------------------------------------
    /**
     * 向名为simple的简单模式队列中发消息：hello RabbitMQ!
     */
    @Test
    void testSimple() {
        rabbitTemplate.convertAndSend("simple", "hello RabbitMQ!");
    }


    //------------------------------------------work.queue 一对多 争抢消费---------------------------------------------------
    /**
     * 向名为work.queue的队列发消息  执行三次，看哪个消费者消费到
     */
    @Test
    void testWorkQueue() {
        rabbitTemplate.convertAndSend("work.queue", "hello work queue!");
    }


    //------------------------------------------fanout交换机 广播模式 交换机把一个消息同时发给多个队列---------------------------------------------------
    /**
     * 向名为fanout.exchange的交换机发消息 执行两次，观察消费者消费情况
     */
    @Test
    void testFanout() {
        String exchange = "fanout.exchange";
        String routingKey = null;
        rabbitTemplate.convertAndSend(exchange, routingKey,  "hello Fanout Exchange!");
    }


    //------------------------------------------direct交换机 定向模式 生产者发送消息时带一个路由键，交换机将消息发给指定路由键的队列---------------------------------------------------
    /**
     * 向名为direct.exchange的交换机发消息 执行三次，每次routingKey不同，观察消费者消费情况
     */
    @Test
    void testDirect() {
        String exchange = "direct.exchange";
//        String routingKey = "red";
//        String routingKey = "blue";
        String routingKey = "yellow";
        rabbitTemplate.convertAndSend(exchange, routingKey,  "hello Direct Exchange!" + routingKey);
    }


    //------------------------------------------topic交换机 话题模式 生产者发送消息时带一个路由键，路由键可以被.分割为多个路由键；交换机将消息发给指定路由键的队列---------------------------------
    //------------------------------------------队列与交换机绑定时，指定的路由键可以使用通配符：# * ---------------------------------------------------
    /**
     * 向名为topic.exchange的交换机发消息 执行四次，每次routingKey不同，观察消费者消费情况
     */
    @Test
    void testTopic() {
        String exchange = "topic.exchange";
        String routingKey = "shanghai.new";
//        String routingKey = "shanghai.weather";
//        String routingKey = "shanghai.weather.minHang";
//        String routingKey = "beijing.new";
//        String routingKey = "beijing.weather";
//        String routingKey = "beijing.weather.haiDian";
        rabbitTemplate.convertAndSend(exchange, routingKey,  "hello Topic Exchange!" + routingKey);
    }
}
