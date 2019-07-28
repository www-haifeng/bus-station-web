package com.shuzhi.websocket.socketvo;

import lombok.Data;

import java.util.List;

/**
 * @ProjectName: bus-station-web
 * @Package: com.shuzhi.websocket.socketvo
 * @ClassName: StatisticsMsgVo统计能耗实体类
 * @Author: 陈鑫晖
 * @Date: 2019/7/19 14:57
 */
@Data
public class StatisticsMsgVo {

    /**
     * 总数
     */
    private Integer total = 0;

    /**
     * 在线
     */
    private Integer online = 0;

    /**
     * 离线
     */
    private Integer offline = 0;

    /**
     * 亮灯数
     */
    private Integer oncount = 0;

    /**
     * 熄灯数
     */
    private Integer offcount = 0;

    /**
     * 本月
     */
    private float currentmonth = 0;

    /**
     * 上月
     */
    private float lastmonth = 0;

    /**
     * 本年
     */
    private float thisyear = 0;

    private List<LcdalarmsVo> lcdalarms;

    private List<LcdalarmsVo> ledalarms;

    private List<LcdalarmsVo> lightalarms;


    public StatisticsMsgVo(float currentmonth, float lastmonth, float thisyear) {
        this.currentmonth = currentmonth;
        this.lastmonth = lastmonth;
        this.thisyear = thisyear;
    }
    public StatisticsMsgVo() {
    }
}
