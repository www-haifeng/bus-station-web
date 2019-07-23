package com.shuzhi.websocket.socketvo;

import lombok.Data;

/**
 * @author zgk
 * @description
 * @date 2019-07-23 17:05
 */
@Data
public class platformStatisVo {

    /**
     * 照明总数
     */
    private Integer lighttotal;

    /**
     * 照明在线
     */
    private Integer lightonline;

    /**
     * 照明离线
     */
    private Integer lightoffline;

    /**
     * led总数
     */
    private Integer ledtotal;

    /**
     * led在线
     */
    private Integer ledonline;

    /**
     * led离线
     */
    private Integer ledoffline;

    /**
     * lcd总数
     */
    private Integer lcdtotal;

    /**
     * lcd在线
     */
    private Integer lcdonline;

    /**
     * lcd离线
     */
    private Integer lcdoffline;

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
}
