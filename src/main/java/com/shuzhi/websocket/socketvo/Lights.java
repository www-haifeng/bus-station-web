package com.shuzhi.websocket.socketvo;

import com.shuzhi.entity.DeviceLoop;
import com.shuzhi.entity.MqMessage;
import com.shuzhi.light.entities.TLoopStateDto;
import com.shuzhi.service.DeviceLoopService;
import com.shuzhi.service.MqMessageService;
import com.shuzhi.websocket.ApplicationContextUtils;
import lombok.Data;

/**
 * @author zgk
 * @description
 * @date 2019-07-15 16:32
 */
@Data
public class Lights {

    /**
     * 公交站id
     */
    private Integer stationid;

    /**
     * 公交站名称
     */
    private String stationname;

    /**
     * 灯箱id
     */
    private Integer lamphouseid;

    /**
     * 灯箱开关
     */
    private Integer lamphouseonoff;

    /**
     * 灯箱在线
     */
    private Integer lamphouseline;

    /**
     * 顶棚id
     */
    private Integer platfondid;

    /**
     * 顶棚开关
     */
    private Integer platfondonoff;

    /**
     * 顶棚在线
     */
    private Integer platfondline;

    /**
     * logoid
     */
    private Integer logoid;

    /**
     * logo开关
     */
    private Integer logoonoff;

    /**
     * logo在线
     */
    private Integer logoline;


    public Lights(TLoopStateDto tLoopStateDto) {

        //通过回路号查询这个是什么设备
        DeviceLoopService deviceLoopService = ApplicationContextUtils.get(DeviceLoopService.class);
        DeviceLoop deviceLoopSelect = new DeviceLoop();
        deviceLoopSelect.setLoop(tLoopStateDto.getLoop());
        DeviceLoop deviceLoop = deviceLoopService.selectOne(deviceLoopSelect);
        //判断这是什么设备
        //TODO 现在还不知道设备对应的编号
        switch (deviceLoop.getTypecode()){
            //灯箱
            case "100001" :
                this.lamphouseid = tLoopStateDto.getId();
                this.lamphouseonoff = tLoopStateDto.getState();
                this.lamphouseline = tLoopStateDto.getState();
                break;
            //顶棚
            case "100002" :
                this.platfondid = tLoopStateDto.getId();
                this.platfondline = tLoopStateDto.getState();
                this.platfondonoff = tLoopStateDto.getState();
                break;
            //log
            case "100003" :
                this.logoid = tLoopStateDto.getId();
                this.logoline = tLoopStateDto.getState();
                this.logoonoff = tLoopStateDto.getState();
            default:

        }
        this.stationid = Integer.valueOf(tLoopStateDto.getGatewayId());
    }
}
