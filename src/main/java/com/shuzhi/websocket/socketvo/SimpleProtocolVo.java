package com.shuzhi.websocket.socketvo;

import lombok.Data;

import java.util.UUID;

/**
 * @author zgk
 * @description 封装简易协议
 * @date 2019-07-12 11:15
 */
@Data
public class SimpleProtocolVo {

    /**
     * 唯一标识
     */
    private String msgid;

    /**
     * 对应lights
     */
    private String did;

    /**
     * 返回值的状态码
     */
    private Integer code;

    /**
     * 命令参数
     */
    private Object data;

    /**
     * 将原始数据转换为简易协议
     *
     * @param messageVo 原始数据
     */
    public SimpleProtocolVo(MessageVo messageVo) {

        this.msgid = messageVo.getMsgid();
      //  this.did = messageVo.getModulecode();
        //
    }


    public SimpleProtocolVo() {
    }
}
