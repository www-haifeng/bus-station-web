package com.shuzhi.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.shuzhi.entity.MqMessage;
import com.shuzhi.mapper.MqMessageMapper;
import com.shuzhi.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author zgk
 * @description 消息队列服务消费者
 * @date 2019-07-07 13:08
 */
@Slf4j
@Component
public class RabbitConsumer {

    private final WebSocketServer webSocketServer;

    private final MqMessageMapper messageMapper;

    public RabbitConsumer(WebSocketServer webSocketServer, MqMessageMapper messageMapper) {
        this.webSocketServer = webSocketServer;
        this.messageMapper = messageMapper;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "lcd-lc", durable = "true"),
            exchange = @Exchange(value = "lcd", durable = "true", type = "topic")
    ))
    @RabbitHandler
    public void consumer(@Payload Message message, @Headers Map<String, Object> headers,
                         Channel channel) throws IOException {

        log.info("--------------收到消息，开始消费------------");
        log.info("消息是 : {}" ,JSON.toJSONString(message));
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        //查出消息属于哪个通道
        MqMessage mqMessageSelect = new MqMessage();
        mqMessageSelect.setSubtype(message.getSubtype());
        mqMessageSelect.setType(message.getType());
        MqMessage mqMessage = messageMapper.selectOne(mqMessageSelect);

        //推送消息
        webSocketServer.send(mqMessage.getName(),JSON.toJSONString(message));
        // ACK
        channel.basicAck(deliveryTag, false);
    }

}
