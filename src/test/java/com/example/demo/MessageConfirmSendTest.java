package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: HanXu
 * on 2024/7/2
 * Class description: 测试消息发送后异步接收ConfirmCallBack 确保消息发送的可靠性
 */
@Slf4j
@SpringBootTest
public class MessageConfirmSendTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    void testConfirm() throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Confirm");
        map.put("age", 20);

        //设置ConfirmCallBack
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable throwable) {
                //MQ回调了，但是到Spring中执行失败了。基本不会发生，不用管
                log.error("消息确认机制回调失败", throwable);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                //MQ回调到服务中了，可能回调的是ack，也可能是nack
                //log.info("消息确认机制回调成功,收到ConfirmCallBack");
                if (confirm.isAck()) {
                    log.info("消息发送成功，收到ack");
                } else {
                    log.error("消息发送失败，收到nack，原因:{}", confirm.getReason());
                    // TODO: 2024/7/2 这里要做消息重发
                }

            }
        });

        rabbitTemplate.convertAndSend("annotation.topic.exchange.obj123", "obj.map1",  map, correlationData);

        //我们消息Confirm是异步的，而单元测试执行完就结束了，所以这里等一下收到Confirm
        Thread.sleep(2000);
    }
}
