package com.example.demo.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

//@Component
public class MessageReceiver {

    //------------------------------------------simple 一对一---------------------------------------------------
    /**
     * 监听队列名为：simple 的消息
     * @param message
     */
    @RabbitListener(queues = "simple")
    public void receiveMessage(String message) {
        // 处理接收到的消息
        System.out.println("Received simple <" + message + ">");
    }


    //------------------------------------------work.queue 一对多 争抢消费---------------------------------------------------
    /**
     * 监听队列名为：work.queue 的消息
     * @param message
     */
    @RabbitListener(queues = "work.queue")
    public void receiveWorkQueue1(String message) {
        System.out.println("Received work.queue <" + message + "> 消费者1");
    }

    /**
     * 监听队列名为：work.queue 的消息
     * @param message
     */
    @RabbitListener(queues = "work.queue")
    public void receiveWorkQueue2(String message) {
        System.out.println("Received work.queue <" + message + "> 消费者2");
    }

    /*
    Received work.queue <hello work queue!> 消费者2
    Received work.queue <hello work queue!> 消费者1
    Received work.queue <hello work queue!> 消费者2
     */


    //------------------------------------------fanout交换机 广播模式 交换机把一个消息同时发给多个队列---------------------------------------------------
    /**
     * 监听队列名为：fanout.exchange.queue1  该队列已与交换机fanout.exchange绑定
     * @param message
     */
    @RabbitListener(queues = "fanout.exchange.queue1")
    public void receiveFanout1(String message) {
        System.out.println("Received Fanout.Exchange.queue1 <" + message + "> 消费者1");
    }

    /**
     * 监听队列名为：fanout.exchange.queue1  该队列已与交换机fanout.exchange绑定 和消费者1争抢队列消息
     * @param message
     */
    @RabbitListener(queues = "fanout.exchange.queue1")
    public void receiveFanout1Back(String message) {
        System.out.println("Received Fanout.Exchange.queue1 <" + message + "> 消费者1_Back");
    }

    /**
     * 监听队列名为：fanout.exchange.queue2  该队列已与交换机fanout.exchange绑定
     * @param message
     */
    @RabbitListener(queues = "fanout.exchange.queue2")
    public void receiveFanout2(String message) {
        System.out.println("Received Fanout.Exchange.queue2 <" + message + "> 消费者2");
    }

    /*
    Received Fanout.Exchange.queue1 <hello Fanout Exchange!> 消费者1
    Received Fanout.Exchange.queue2 <hello Fanout Exchange!> 消费者2

    Received Fanout.Exchange.queue2 <hello Fanout Exchange!> 消费者2
    Received Fanout.Exchange.queue1 <hello Fanout Exchange!> 消费者1_Back
     */


    //------------------------------------------direct交换机 定向模式 生产者发送消息时带一个路由键，交换机将消息发给指定路由键的队列---------------------------------------------------
    /**
     * 监听队列名为：direct.exchange.queue1 该队列已与交换机direct.exchange绑定，指定的routingKey为 red和blue
     * @param message
     */
    @RabbitListener(queues = "direct.exchange.queue1")
    public void receiveDirect1(String message) {
        System.out.println("Received Direct.Exchange.queue1 <" + message + "> 消费者");
    }

    /**
     * 监听队列名为：direct.exchange.queue2 该队列已与交换机direct.exchange绑定，指定的routingKey为 blue和yellow
     * @param message
     */
    @RabbitListener(queues = "direct.exchange.queue2")
    public void receiveDirect2(String message) {
        System.out.println("Received Direct.Exchange.queue2 <" + message + "> 消费者");
    }

    /*
    Received Direct.Exchange.queue1 <hello Direct Exchange!red> 消费者

    Received Direct.Exchange.queue1 <hello Direct Exchange!blue> 消费者
    Received Direct.Exchange.queue2 <hello Direct Exchange!blue> 消费者

    Received Direct.Exchange.queue2 <hello Direct Exchange!yellow> 消费者
     */


    //------------------------------------------topic交换机 话题模式 生产者发送消息时带一个路由键，路由键可以被.分割为多个路由键；交换机将消息发给指定路由键的队列---------------------------------
    //------------------------------------------队列与交换机绑定时，指定的路由键可以使用通配符：# * ---------------------------------------------------
    /**
     * 监听队列名为：topic.exchange.queue1 该队列已与交换机topic.exchange绑定，指定的routingKey为 shanghai
     * @param message
     */
    @RabbitListener(queues = "topic.exchange.queue1")
    public void receiveTopic1(String message) {
        System.out.println("Received Topic.Exchange.queue1 <" + message + "> 消费者");
    }

    /**
     * 监听队列名为：topic.exchange.queue2 该队列已与交换机topic.exchange绑定，指定的routingKey为 beijing.# 会收到所有以beijing开头的routingKey的消息
     * @param message
     */
    @RabbitListener(queues = "topic.exchange.queue2")
    public void receiveTopic2(String message) {
        System.out.println("Received Topic.Exchange.queue2 <" + message + "> 消费者");
    }

    /**
     * 监听队列名为：topic.exchange.queue3 该队列已与交换机topic.exchange绑定，指定的routingKey为 shanghai.* 会收到shanghai.后面加一个单词作为routingKey的消息
     * @param message
     */
    @RabbitListener(queues = "topic.exchange.queue3")
    public void receiveTopic3(String message) {
        System.out.println("Received Topic.Exchange.queue3 <" + message + "> 消费者");
    }

    /*
    Received Topic.Exchange.queue3 <hello Topic Exchange!shanghai.new> 消费者

    Received Topic.Exchange.queue3 <hello Topic Exchange!shanghai.weather> 消费者

# shanghai.weather.minHang 的消息没有消费者收到

    Received Topic.Exchange.queue2 <hello Topic Exchange!beijing.new> 消费者

    Received Topic.Exchange.queue2 <hello Topic Exchange!beijing.weather> 消费者

    Received Topic.Exchange.queue2 <hello Topic Exchange!beijing.weather.haiDian> 消费者
     */
}
