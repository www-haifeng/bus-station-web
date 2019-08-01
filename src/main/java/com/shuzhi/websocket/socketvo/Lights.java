package com.shuzhi.websocket.socketvo;

import com.shuzhi.entity.DeviceLoop;
import com.shuzhi.entity.DeviceStation;
import com.shuzhi.entity.MqMessage;
import com.shuzhi.light.entities.TLoopStateDto;
import com.shuzhi.service.DeviceLoopService;
import com.shuzhi.service.DeviceStationService;
import com.shuzhi.service.MqMessageService;
import com.shuzhi.service.StationService;
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
    private Long stationid = 0L;

    /**
     * 公交站名称
     */
    private String stationname = "该设备没有配置站台";

    /**
     * 设备名称
     */
    private String name;

    /**
     * 灯箱id
     */
    private Long lamphouseid;

    /**
     * 灯箱开关
     */
    private Integer lamphouseonoff;

    /**
     * 灯箱在线
     */
    private Long lamphouseline;

    /**
     * 顶棚id
     */
    private Long platfondid;

    /**
     * 顶棚开关
     */
    private Integer platfondonoff;

    /**
     * 顶棚在线
     */
    private Long platfondline;

    /**
     * logoid
     */
    private Long logoid;

    /**
     * logo开关
     */
    private Integer logoonoff;

    /**
     * logo在线
     */
    private Long logoline;

    /**
     * id
     */
    private Long id;


    public Lights(TLoopStateDto tLoopStateDto) {

        //通过回路号查询这个是什么设备
        DeviceLoopService deviceLoopService = ApplicationContextUtils.get(DeviceLoopService.class);
        DeviceStationService deviceStationService = ApplicationContextUtils.get(DeviceStationService.class);
        DeviceLoop deviceLoopSelect = new DeviceLoop();
        deviceLoopSelect.setLoop(tLoopStateDto.getLoop());
        deviceLoopSelect.setGatewayDid(tLoopStateDto.getGatewayId());
        DeviceLoop deviceLoop = deviceLoopService.selectOne(deviceLoopSelect);
        //查出 对应的公交站id和名称
        DeviceStation deviceStationSelect = new DeviceStation(String.valueOf(deviceLoop.getDeviceDid()));
        DeviceStation deviceStation = deviceStationService.selectOne(deviceStationSelect);
        StationService stationService = ApplicationContextUtils.get(StationService.class);
        if (deviceStation != null){
            this.stationid = Long.valueOf(deviceStation.getStationid());
            this.stationname = stationService.selectByPrimaryKey(deviceStation.getId()).getStationName();
        }
        //判断这是什么设备
        switch (deviceLoop.getTypecode()){
            //灯箱
            case "3" :
                this.lamphouseid = Long.valueOf(deviceLoop.getDeviceDid());
                if (tLoopStateDto.getState() == 1){
                    this.lamphouseonoff = 0;
                    this.lamphouseline = 0L;
                }else {
                    this.lamphouseonoff = 1;
                    this.lamphouseline = 1L;
                }
                break;
            //顶棚
            case "1" :
                this.platfondid = Long.valueOf(deviceLoop.getDeviceDid());
                if (tLoopStateDto.getState() == 1){
                    this.platfondline = 0L;
                    this.platfondonoff = 0;
                }else {
                    this.platfondline = 1L;
                    this.platfondonoff = 1;
                }
                break;
            //log
            case "2" :
                this.logoid = Long.valueOf(deviceLoop.getDeviceDid());
                if (tLoopStateDto.getState() == 1){
                    this.logoline = 0L;
                    this.logoonoff = 0;
                }else {
                    this.logoline = 1L;
                    this.logoonoff = 1;
                }

            default:
        }
        this.name = deviceLoop.getDeviceName();
        this.id = Long.valueOf(deviceLoop.getDeviceDid());
        //this.stationid = Integer.valueOf(tLoopStateDto.getGatewayId());
    }
}
