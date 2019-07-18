package com.shuzhi.websocket.socketvo;

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



}
