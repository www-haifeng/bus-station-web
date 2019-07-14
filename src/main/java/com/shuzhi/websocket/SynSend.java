package com.shuzhi.websocket;

import com.shuzhi.rabbitmq.RabbitProducer;
import com.shuzhi.websocket.socketvo.SimpleProtocolVo;

/**
 * @author zgk
 * @description 异步推送消息
 * @date 2019-07-12 13:49
 */
public class SynSend extends Thread {

    private RabbitProducer rabbitProducer;

    private SimpleProtocolVo message;

    private Integer modulecode;

    SynSend(RabbitProducer rabbitProducer, SimpleProtocolVo message ,Integer modulecode) {
        this.rabbitProducer = rabbitProducer;
        this.message = message;
        this.modulecode = modulecode;
    }

    @Override
    public void run() {
        super.run();
        rabbitProducer.sendMessage(message,modulecode);
    }
}
