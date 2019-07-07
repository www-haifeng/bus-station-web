package com.shuzhi.websocket;

import com.alibaba.fastjson.JSON;
import com.shuzhi.rabbitmq.Message;
import com.shuzhi.rabbitmq.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author zgk
 * @description
 * @date 2019-07-07 16:13
 */
@Slf4j
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {

    private final RabbitProducer rabbitProducer;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    public WebSocketServer(RabbitProducer rabbitProducer) {
        this.rabbitProducer = rabbitProducer;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @param sid     接收到的sid
     */
    @OnOpen
    public void open(Session session, @PathParam("sid") String sid) {
        this.session = session;
    }

    @OnClose
    public void close() {
        log.info("连接已关闭");
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void message(String message, Session session) {
        log.info("收到来自窗口的信息: {}", message);
        rabbitProducer.sendMessage(JSON.parseObject(message, Message.class));
    }

    @OnError
    public void error(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
