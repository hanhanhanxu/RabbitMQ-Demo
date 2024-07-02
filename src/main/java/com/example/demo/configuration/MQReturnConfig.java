package com.example.demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MQReturnConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        //正常编写测试好的代码之后，是不会触发的
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息投递失败,返回码:{},原因:{},交换机:{},路由键:{},消息体:{}",
                    replyCode, replyText, exchange, routingKey, message);
        }));
    }
}
