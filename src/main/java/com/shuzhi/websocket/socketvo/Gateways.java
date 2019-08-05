package com.shuzhi.websocket.socketvo;

import com.shuzhi.entity.DeviceLoop;
import com.shuzhi.entity.DeviceStation;
import com.shuzhi.entity.Station;
import com.shuzhi.light.entities.LoopMonitor;
import com.shuzhi.light.entities.StatisticsVo;
import com.shuzhi.light.service.LoopStatusServiceApi;
import com.shuzhi.mapper.DeviceLoopMapper;
import com.shuzhi.mapper.DeviceStationMapper;
import com.shuzhi.mapper.StationMapper;
import com.shuzhi.websocket.ApplicationContextUtils;
import com.shuzhi.websocket.Statistics;
import lombok.Data;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * @author zgk
 * @description 用电管理所有站集中控制器信息
 * @date 2019-08-04 10:20
 */
@Data
public class Gateways {

    /**
     * 公交站id
     */
    private Integer stationid;

    /**
     * 公交站名称
     */
    private String stationname;

    /**
     * 集中控制器id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 本月
     */
    private float currentmonth;

    /**
     * 上月
     */
    private float lastmonth;

    /**
     * 本年
     */
    private float thisyear;

    public Gateways(Station station) {

        //查询出该站下所有的设备
        LoopStatusServiceApi loopStatusServiceApi = ApplicationContextUtils.get(LoopStatusServiceApi.class);
        DeviceStationMapper deviceStationMapper = ApplicationContextUtils.get(DeviceStationMapper.class);
        DeviceLoopMapper deviceLoopMapper = ApplicationContextUtils.get(DeviceLoopMapper.class);
        DeviceStation deviceStation = new DeviceStation();
        deviceStation.setStationid(station.getId());
        List<DeviceStation> select = deviceStationMapper.select(deviceStation);
        //查出是哪个回路和网关
        DeviceLoop deviceLoop = new DeviceLoop();
        StatisticsVo statisticsVo = new StatisticsVo();
        select.forEach(deviceStation1 -> {
            deviceLoop.setDeviceDid(deviceStation1.getDeviceDid());
            deviceLoop.setTypecode(deviceStation1.getTypecode());
            DeviceLoop deviceLoop1 = deviceLoopMapper.selectOne(deviceLoop);
            //算出能耗
            statisticsVo.setLoop(deviceLoop1.getLoop());
            statisticsVo.setDid(deviceLoop1.getDeviceDid());
            try {
                StatisticsMsgVo statistics = Statistics.findStatistics(statisticsVo);
                this.currentmonth = this.currentmonth + statistics.getCurrentmonth();
                this.lastmonth = this.lastmonth + statistics.getLastmonth();
                this.thisyear = this.thisyear + statistics.getThisyear();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.id = deviceLoop1.getGatewayDid();
        });
        //查出所有网关状态信息
        List<LoopMonitor> redis = loopStatusServiceApi.findRedis();
        redis.forEach(loopMonitor -> {

        });

        this.stationid = station.getId();
        this.stationname = station.getStationName();
        this.name = stationname;

    }
}
