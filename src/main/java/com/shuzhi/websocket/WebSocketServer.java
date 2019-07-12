package com.shuzhi.websocket;

import com.alibaba.fastjson.JSON;
import com.shuzhi.rabbitmq.Message;
import com.shuzhi.rabbitmq.RabbitProducer;
import com.shuzhi.websocket.socketvo.ReceiptHandleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zgk
 * @description websocket
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
    public void onOpen(Session session) {

        //推送消息到页面
        Optional.ofNullable(session).ifPresent(session1 -> {
            try {
                session1.getBasicRemote().sendText("连接建立成功");
            } catch (IOException e) {
                e.printStackTrace();
                log.error("连接建立失败 sessionId : {}", session1.getId());
            }
        });
    }


    /**
     * 接收消息的方法
     *
     * @param message 收到的消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {

        //获取模块id
        Message message1 = JSON.parseObject(message, Message.class);
        String token = String.valueOf(message1.getModulecode());
        SESSION_MAP.put(token, session);
        log.info("当前的session数量 : {}", SESSION_MAP.size());
        SESSION_MAP.entrySet().stream().filter(stringSessionEntry -> session.getId().equals(stringSessionEntry.getValue().getId()))
                .forEach(stringSessionEntry -> log.info("{} 发来消息", stringSessionEntry.getKey()));
        //判断消息类型
        switch (message1.getMsgtype()) {
            //请求
            case "2":
                restHandle(message1);
                break;
            //回执
            case "3":
                receiptHandle(message1);
                break;
            default:
                throw new Exception("消息类型错误");
        }
    }

    /**
     * 处理回执消息
     *
     * @param message 消息
     */
    private void receiptHandle(Message message) {

        //

    }

    /**
     * 处理响应消息
     *
     * @param message 消息
     */
    private void restHandle(Message message) {

        Integer modulecode = message.getModulecode();
        //判断消息编码
        switch (message.getMsgcode()) {
            case 200001:
                break;
            //首次建立连接
            case 200000:
                ReceiptHandleVo receiptHandleVo = new ReceiptHandleVo(message.getMsgid(), modulecode, new Date());
                send(String.valueOf(modulecode), JSON.toJSONString(receiptHandleVo));

            default:
        }

    }

    /**
     * 处理推送消息
     *
     * @param message 消息
     */
    private void pushHandle(String message) {

        //发送消息
        RabbitProducer rabbitProducer = ApplicationContextUtils.get(RabbitProducer.class);
        //生成一个唯一标识 并保存到redis中 然后发送消息
        //rabbitProducer.sendMessage(setMsgId(message));

    }

    /**
     * 生成唯一标识 并保存在redis中
     *
     * @param message 接收到的数据
     * @return 要发送的对象
     */
    private Message setMsgId(String message) {

        //获取redisTemplate对象
        Optional.ofNullable(redisTemplate).orElseGet(() -> redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class));
        //获取随机UUID
        Message message1 = JSON.parseObject(message, Message.class);
        String msgId = UUID.randomUUID().toString();
        //存入redis中
        redisTemplate.opsForHash().put("web_socket_key", msgId, message1.getModulecode());
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
        Optional.ofNullable(redisTemplate).orElseGet(() -> redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class));
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
            //推送消息到页面
            Optional.ofNullable(SESSION_MAP.get(token)).ifPresent(session -> {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("消息推送失败 : {}", e.getMessage());
                }
            });
            log.info("消息发送成功 : {} , {}", message, new Date());
            //删除缓存
            if (redisTemplate == null) {
                redisTemplate = ApplicationContextUtils.get(StringRedisTemplate.class);
            }
            redisTemplate.opsForHash().delete("web_socket_key", JSON.parseObject(message, Message.class).getMsgid());
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
