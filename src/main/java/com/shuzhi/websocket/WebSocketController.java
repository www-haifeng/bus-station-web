package com.shuzhi.websocket;

import com.alibaba.fastjson.JSON;
import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.entity.MqMessage;
import com.shuzhi.mapper.MqMessageMapper;
import com.shuzhi.rabbitmq.RabbitProducer;
import com.shuzhi.websocket.socketvo.MessageVo;
import com.shuzhi.websocket.socketvo.SimpleProtocolVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zgk
 * @description 接收命令信息
 * @date 2019-07-11 13:39
 */
@RestController
@RequestMapping("/webSocket")
public class WebSocketController{

    private final MqMessageMapper messageMapper;

    private final RabbitProducer rabbitProducer;

    public WebSocketController(MqMessageMapper messageMapper, RabbitProducer rabbitProducer) {
        this.messageMapper = messageMapper;
        this.rabbitProducer = rabbitProducer;
    }

    /**
     * 命令操作
     *
     * @param messageVo 命令详情
     * @return 操作结果
     */
    @RequestMapping("/command")
    public WrapMapper command(MessageVo messageVo){

        //拼装数据并发送
        withMessage(messageVo);

        return null;
    }

    /**
     * 拼装消息并发送
     *
     * @param messageVo 未处理前的消息
     */
    private void withMessage(MessageVo messageVo) {

        SimpleProtocolVo message = new SimpleProtocolVo();

        message = JSON.parseObject("{\n" +
                "    \"did\": \"867725032979092\",\n" +
                "    \"cmdid \": \"10001\",\n" +
                "    \"msgid\": \"f04d8ecc-565f-4a3a-bc0b-a583a990bd87\",\n" +
                "    \"data\": {\n" +
                "        \"cmdid\": \"10001\"\n" +
                "    }\n" +
                "}", SimpleProtocolVo.class);

        //发送简易协议
        SynSend synSend = new SynSend(rabbitProducer,message,200001);
        synSend.start();


    }

}
