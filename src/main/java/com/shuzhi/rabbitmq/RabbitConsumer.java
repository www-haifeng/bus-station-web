package com.shuzhi.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.shuzhi.mapper.MqMessageMapper;
import com.shuzhi.websocket.WebSocketServer;
import org.springframework.amqp.core.AmqpTemplate;
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
@Component
public class RabbitConsumer {

    private final WebSocketServer webSocketServer;

    public RabbitConsumer(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "lcd-lc", durable = "true"),
            exchange = @Exchange(value = "lcd", durable = "true", type = "topic")
    ))
    @RabbitHandler
    public void consumer(@Payload Message message, @Headers Map<String, Object> headers,
                         Channel channel) throws IOException {

        System.out.println("--------------收到消息，开始消费------------");
        System.out.println("消息ID是：" + message.getMsgid());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //推送消息
        webSocketServer.sendMessage(JSON.toJSONString(message));
        // ACK
        channel.basicAck(deliveryTag, false);
    }

}
