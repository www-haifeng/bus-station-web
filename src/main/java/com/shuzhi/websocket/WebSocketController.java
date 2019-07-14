package com.shuzhi.websocket;

import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.DeviceLoop;
import com.shuzhi.rabbitmq.RabbitProducer;
import com.shuzhi.service.DeviceLoopService;
import com.shuzhi.websocket.socketvo.MessageVo;
import com.shuzhi.websocket.socketvo.Msg;
import com.shuzhi.websocket.socketvo.SimpleProtocolVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author zgk
 * @description 接收命令信息
 * @date 2019-07-11 13:39
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Slf4j
@RestController
@RequestMapping("/webSocket")
public class WebSocketController {

    private final RabbitProducer rabbitProducer;

    private final DeviceLoopService deviceLoopService;

    public WebSocketController(RabbitProducer rabbitProducer, DeviceLoopService deviceLoopService) {
        this.rabbitProducer = rabbitProducer;
        this.deviceLoopService = deviceLoopService;
    }

    /**
     * 命令操作
     *
     * @param messageVo 命令详情
     * @return 操作结果
     */
    @RequestMapping("/command")
    public Wrapper command(MessageVo messageVo) {

        //拼装消息
        List<SimpleProtocolVo> messageList = assemble(messageVo);
        //遍历并发送简易协议
        Objects.requireNonNull(messageList).forEach(message -> new SynSend(rabbitProducer, message, messageVo.getModulecode()).start());
        return WrapMapper.ok();
    }

    /**
     * 拼装简易协议
     *
     * @param messageVo 前端协议
     * @return 简易协议
     */
    private List<SimpleProtocolVo> assemble(MessageVo messageVo) {
        //判断是led 还是lcd 还是 照明
        List<SimpleProtocolVo> simpleProtocolVos = new ArrayList<>();
        Msg msg = messageVo.getMsg();

        SimpleProtocolVo simpleProtocolVo = new SimpleProtocolVo();
        //msgId
        simpleProtocolVo.setMsgid(UUID.randomUUID().toString());
        //lcd设备
        lcdEquip(simpleProtocolVos, msg, simpleProtocolVo);
        //led设备
        ledEquip(simpleProtocolVos, msg, simpleProtocolVo);
        //照明设备 照明设备没有重启操作和音量调节
        if (msg.getCmdtype() != 10007) {
            light(simpleProtocolVos, msg, simpleProtocolVo);
        }
        return simpleProtocolVos;
    }


    /**
     * 照明设备封装简易协议 提取重复代码
     *
     * @param msg              报文数据
     * @param simpleProtocolVo 简易协议
     */
    private void light(List<SimpleProtocolVo> simpleProtocolVos, Msg msg, SimpleProtocolVo simpleProtocolVo) {
        Optional.ofNullable(msg.getLights()).ifPresent(lights -> {
            DeviceLoop deviceLoopSelect = new DeviceLoop();
            lights.forEach(light -> {
                //通过设备id查出回路和网关id
                deviceLoopSelect.setDeviceDid(Integer.valueOf(light));
                DeviceLoop deviceLoop = deviceLoopService.selectOne(deviceLoopSelect);
                //网关id
                simpleProtocolVo.setDid(String.valueOf(deviceLoop.getGatewayDid()));
                HashMap<String, Object> data = new HashMap<>(3);
                //开灯
                if (msg.getCmdtype() == 1){
                    data.put("cmdid", "10001");
                }else {
                    //关灯
                    data.put("cmdid", "10002");
                }
                //回路
                data.put("loop", deviceLoop.getLoop());
                //是否闭合
                if (msg.getCmdtype() == 1) {
                    data.put("state", 1);
                } else {
                    data.put("state", 0);
                }
                simpleProtocolVo.setData(data);
                simpleProtocolVos.add(simpleProtocolVo);
            });
        });
    }

    /**
     * led设备封装简易协议 提取重复代码
     *
     * @param msg              报文数据
     * @param simpleProtocolVo 简易协议
     */
    private void ledEquip(List<SimpleProtocolVo> simpleProtocolVos, Msg msg, SimpleProtocolVo simpleProtocolVo) {
        Optional.ofNullable(msg.getLeds()).ifPresent(leds -> {
            //拼装数据
            leds.forEach(led -> {
                //设备编号
                simpleProtocolVo.setDid(led);
                //亮度和音量 重启操作没有亮度和音量
                if (msg.getCmdtype() != 3) {
                    switch (msg.getCmdtype()) {
                        //开灯
                        case 1:
                            simpleProtocolVo.setCmdid("10001");
                            simpleProtocolVo.setData(new HashMap<>(1).put("arg1", 1));
                            break;
                        //关灯
                        case 2:
                            simpleProtocolVo.setCmdid("10002");
                            simpleProtocolVo.setData(new HashMap<>(1).put("arg1", 0));
                            break;
                        //调光
                        case 4:
                            simpleProtocolVo.setCmdid("10003");
                            simpleProtocolVo.setData(new HashMap<>(1).put("arg1", msg.getLight()));
                            break;
                        //音量
                        case 5:
                            simpleProtocolVo.setCmdid("10004");
                            simpleProtocolVo.setData(new HashMap<>(1).put("arg1", msg.getVolume()));
                            break;
                        default:
                    }
                } else {
                    //重启
                    simpleProtocolVo.setCmdid("10005");
                }
                simpleProtocolVos.add(simpleProtocolVo);
            });
        });
    }

    /**
     * lcd设备封装简易协议 提取重复代码
     *
     * @param msg              报文数据
     * @param simpleProtocolVo 简易协议
     */
    private void lcdEquip(List<SimpleProtocolVo> simpleProtocolVos, Msg msg, SimpleProtocolVo simpleProtocolVo) {
        Optional.ofNullable(msg.getLcds()).ifPresent(lcds -> {
            //拼装数据
            lcds.forEach(lcd -> {
                //设备编号
                simpleProtocolVo.setDid(lcd);
                simpleProtocolVo.setData(new HashMap<>(1).put("cids", lcd));
                //lcd设备没有调光
                if (msg.getCmdtype() != 4) {
                    switch (msg.getCmdtype()) {
                        //开灯
                        case 1:
                            simpleProtocolVo.setCmdid("10001");
                            break;
                        //关灯
                        case 2:
                            simpleProtocolVo.setCmdid("10002");
                            break;
                        //重启
                        case 3:
                            simpleProtocolVo.setCmdid("10004");
                            break;
                        //音量
                        case 5:
                            simpleProtocolVo.setCmdid("10005");
                            simpleProtocolVo.setData(new HashMap<>(1).put("vol", msg.getVolume()));
                            break;
                        default:
                    }
                }
            });
            simpleProtocolVos.add(simpleProtocolVo);
        });
    }
}
