package com.shuzhi.websocket;

import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.entity.MqMessage;
import com.shuzhi.mapper.MqMessageMapper;
import com.shuzhi.websocket.socketvo.MessageVo;
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
public class WebSocketController {

    private final MqMessageMapper messageMapper;

    public WebSocketController(MqMessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    /**
     * 命令操作
     *
     * @param messageVo 命令详情
     * @return 操作结果
     */
    @RequestMapping("/command")
    public WrapMapper command(MessageVo messageVo){
        //通过模块编码查出遥望哪里发送
        MqMessage mqMessageSelect = new MqMessage();
        mqMessageSelect.setModulecode(messageVo.getModulecode());
        MqMessage mqMessage = messageMapper.selectOne(mqMessageSelect);

        return null;
    }
}
