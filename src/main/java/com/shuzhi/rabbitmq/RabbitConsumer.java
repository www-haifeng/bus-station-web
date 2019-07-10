package com.shuzhi.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.shuzhi.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    private final StringRedisTemplate redisTemplate;

    public RabbitConsumer(WebSocketServer webSocketServer, StringRedisTemplate redisTemplate) {
        this.webSocketServer = webSocketServer;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "equip-up", durable = "true"),
            exchange = @Exchange(value = "equip", durable = "true", type = "topic")
    ))
    @RabbitHandler
    public void consumer(@Payload String message, @Headers Map<String, Object> headers,
                         Channel channel) throws IOException {

        log.info("--------------收到消息，开始消费------------");
        log.info("消息是 : {}", message);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        Message message1 = JSON.parseObject(message, Message.class);
        message1.setMsgtype("2");
        //推送消息
        String key = (String) redisTemplate.opsForHash().get("web_socket_key", message1.getMsgid());
        if (StringUtils.isNotBlank(key)){
            webSocketServer.send(key,JSON.toJSONString(message1));
        }else {
            log.warn("msgId不存在 : {}",message1.getMsgid());
        }
        // ACK
        channel.basicAck(deliveryTag, false);
    }

}
