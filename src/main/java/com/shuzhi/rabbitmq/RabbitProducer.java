package com.shuzhi.rabbitmq;

import com.shuzhi.entity.MqMessage;
import com.shuzhi.mapper.MqMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zgk
 * @description 消息队列服务生产者
 * @date 2019-07-07 13:08
 */
@Slf4j
@Component
public class RabbitProducer implements  ApplicationRunner {

    private final MqMessageMapper messageMapper;

    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public RabbitProducer(AmqpTemplate rabbitTemplate, MqMessageMapper messageMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageMapper = messageMapper;
    }

    /**
     * 向消息队列推送消息
     *
     * @param message 消息实体
     */
    public void sendMessage(Message message){
        //查出Exchange 和topic
        MqMessage mqMessageSelect = new MqMessage();
        mqMessageSelect.setSubtype(message.getSubtype());
        MqMessage mqMessage = messageMapper.selectOne(mqMessageSelect);
        rabbitTemplate.convertAndSend(mqMessage.getExchange(),mqMessage.getTopic() , message);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {

        Message message = new Message();
        message.setType("111");
        message.setSubtype("111");
        message.setMsgid("这是个测试消息");
        log.info("---------------自动执行测试------------------");
        sendMessage(message);
    }
}
