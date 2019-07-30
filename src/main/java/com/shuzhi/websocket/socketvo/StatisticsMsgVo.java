package com.shuzhi.websocket.socketvo;

import com.shuzhi.led.entities.TStatusDto;
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
     * 开关状态
     */
    private Integer onoff = 0;

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

    /**
     * 当前
     */
    private float activepowerNow = 0;

    private List<LcdalarmsVo> lcdalarms;

    private List<LcdalarmsVo> ledalarms;

    private List<LcdalarmsVo> lightalarms;


    public StatisticsMsgVo(float currentmonth, float lastmonth, float thisyear ,float activepowerNow) {
        this.currentmonth = currentmonth;
        this.lastmonth = lastmonth;
        this.thisyear = thisyear;
        this.activepowerNow = activepowerNow;
    }
    public StatisticsMsgVo() {
    }

    public StatisticsMsgVo(float currentmonth, float lastmonth, float thisyear) {

        this.currentmonth = currentmonth;
        this.lastmonth = lastmonth;
        this.thisyear = thisyear;

    }

    public void addNum(List<TStatusDto> allStatus) {

        this.total = allStatus.size();
        this.online = Math.toIntExact(allStatus.stream().filter(tStatusDto -> tStatusDto.getOnoff() == 1).count());
        this.offline = Math.toIntExact(allStatus.stream().filter(tStatusDto -> tStatusDto.getOnoff() == 0).count());

    }
}
