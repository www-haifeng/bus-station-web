package com.shuzhi.websocket;

import com.alibaba.fastjson.JSON;
import com.shuzhi.rabbitmq.Message;
import com.shuzhi.rabbitmq.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zgk
 * @description
 * @date 2019-07-07 16:13
 */
@Slf4j
@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {

    private StringRedisTemplate redisTemplate;

    private static Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen() {
        log.info("建立连接成功");
    }


    /**
     * 接收消息的方法
     *
     * @param message 收到的消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        //获取模块id
        Message message1 = JSON.parseObject(message, Message.class);
        String token = String.valueOf(message1.getModulecode());
        SESSION_MAP.put(token, session);
        log.info("当前的session数量 : {}", SESSION_MAP.size());
        //noinspection LoopStatementThatDoesntLoop
        for (Map.Entry<String, Session> entry : SESSION_MAP.entrySet()) {
            log.info("session.id : {}", session.getId());
            if (session.getId().equals(entry.getValue().getId())) {
                log.info("{}发来消息", entry.getKey());
            }
            break;
        }
        //判断消息类型
       /* switch (message1.getMsgtype()) {
            //推送
            case "1":
                break;
            //请求
            case "2":
                break;
            //回执
            case "3":
                break;
            default:
                throw new Exception("消息类型错误");
        }*/
        //发送消息
        RabbitProducer rabbitProducer = ApplicationContextUtils.get(RabbitProducer.class);
        //生成一个唯一标识 并保存到redis中 然后发送消息
        rabbitProducer.sendMessage(setMsgId(message, token));

    }

    /**
     * 生成唯一标识 并保存在redis中
     *
     * @param message 接收到的数据
     * @param token   页面标识
     * @return 要发送的对象
     */
    private Message setMsgId(String message, String token) {

        if (redisTemplate == null) {
            redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class);
        }
        //获取随机UUID
        Message message1 = JSON.parseObject(message, Message.class);
        String msgId = UUID.randomUUID().toString();
        //存入redis中
        redisTemplate.opsForHash().put("web_socket_key", msgId, token);
        message1.setMsgid(msgId);
        log.info("接收到消息 : {}", JSON.toJSONString(message1));
        return message1;
    }

    /**
     * 关闭连接的方法
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("有连接关闭");
        Map<String, Session> map = new HashMap<>(16);
        SESSION_MAP.forEach((k, v) -> {
            if (!session.getId().equals(v.getId())) {
                map.put(k, v);
            }
        });
        SESSION_MAP = map;
        //删除缓存
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class);
        }
        redisTemplate.opsForHash().delete("web_socket_key");
        log.info("断开连接后session数量 : {}", SESSION_MAP.size());
    }

    /**
     * 发送消息的方法
     *
     * @param token   接收到的参数
     * @param message 要发送的信息
     */
    public void send(String token, String message) {

        if (StringUtils.isNotBlank(token)) {
            try {
                //推送消息到页面
                Session session = SESSION_MAP.get(token);
                if (session != null){
                    session.getBasicRemote().sendText(message);
                    log.info("消息发送成功 : {} , {}", message,new Date());
                }
                //删除缓存
                if (redisTemplate == null) {
                    redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class);
                }
                redisTemplate.opsForHash().delete("web_socket_key", JSON.parseObject(message, Message.class).getMsgid());
            } catch (IOException e) {
                log.error("发送信息错误 : {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误调用的方法
     *
     * @param session session
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误 sessionId : {} ", session.getId());
        error.printStackTrace();
    }

    /**
     * 获取设备信息
     *
     * @param token 页面标识
     * @return 设备信息的json
     */
    private String getEquipment(String token) {

        //判断是哪个设备
        switch (token) {
            case "10001":
                break;

            case "10002":
                break;

            case "10003":
                break;

            default:
        }
        return null;
    }
}
