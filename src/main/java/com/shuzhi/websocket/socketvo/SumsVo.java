package com.shuzhi.websocket.socketvo;

import lombok.Data;

/**
 * @author zgk
 * @description 单个站台的统计信息
 * @date 2019-07-30 10:37
 */
@Data
public class SumsVo {

    /**
     * 公交站id
     */
    private Integer stationid;

    /**
     * 公交站名称
     */
    private String stationname;

    /**
     * 本月能耗
     */
    private float currentmonth = 0;

    /**
     * 上月能耗
     */
    private float lastmonth = 0;

    /**
     * 本年
     */
    private float thisyear = 0;

    /**
     * 灯箱能耗
     */
    private float lamphouse = 0;

    /**
     * 顶棚能耗
     */
    private float platfond = 0;

    /**
     * logo能耗
     */
    private float logo = 0;

    /**
     * led能耗
     */
    private float led = 0;

    /**
     * lcd能耗
     */
    private float lcd = 0;

}
