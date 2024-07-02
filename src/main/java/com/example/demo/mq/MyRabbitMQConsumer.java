package com.example.demo.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class MyRabbitMQConsumer implements ChannelAwareMessageListener {

    @Override
    @RabbitListener(queues = "myQueue")
    public void onMessage(Message message, Channel channel) throws Exception {
        //RabbitMQ自动分配的，递增的，每个消费者通道唯一
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 消息处理逻辑
            String messageBody = new String(message.getBody());
            System.out.println("Received message: " + messageBody);

            // 模拟消息处理
            if ("ack_messsage".equals(messageBody)) {
                channel.basicAck(deliveryTag, false);
            } else if ("nack_messsage".equals(messageBody)) {
                channel.basicNack(deliveryTag, false, true);
            } else if ("reject_messsage".equals(messageBody)) {
                channel.basicReject(deliveryTag, false);
            }
        } catch (Exception e) {
            // 处理消息失败时，可以选择重新投递或丢弃
            channel.basicNack(deliveryTag, false, true);  // 重新投递
            // 或者
            // channel.basicReject(deliveryTag, false);  // 丢弃
            e.printStackTrace();
        }
    }
}