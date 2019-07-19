package com.shuzhi.websocket;

import com.shuzhi.light.entities.TElectricQuantity;
import com.shuzhi.light.entities.TLoopStateDto;
import com.shuzhi.light.service.LoopStatusServiceApi;
import com.shuzhi.websocket.socketvo.StatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: bus-station-web
 * @Package: com.shuzhi.websocket
 * @ClassName: Statistics
 * @Author: 陈鑫晖
 * @Date: 2019/7/15 15:12
 */
@RestController
public class Statistics {

    @Autowired
    private LoopStatusServiceApi loopStatusServiceApi;

    //private LightClientService lightClientService;
    /**
     * 查询回路状态信息
     * @return
     */
    @RequestMapping(value ="findLoop",method = RequestMethod.GET)
    public List<TLoopStateDto> findLoop(){
        List<TLoopStateDto> loopStatus = loopStatusServiceApi.findLoopStatus();
        return loopStatus;
    }

    /**
     * 查询回路能耗
     * @param statisticsVo
     * @return
     */
    public List<TElectricQuantity> findStatistics(@RequestBody StatisticsVo statisticsVo){

        //设置当前月份时间
        Date date = new Date();
        statisticsVo.setStartTime(date);
        //上个月第一天和上个月最后一天
        Map<String, String> map = Statistics.getFirstday_Lastday_Month(date);
        try {
            //取出上个月最后一天
            String day_last = map.get("last");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(day_last);
            statisticsVo.setEndTime(date1);
            //获取本月能耗所有信息
            List<TElectricQuantity> electricQuantityNowMonth = loopStatusServiceApi.findElectricQuantity(statisticsVo.getDid(),statisticsVo.getHid());
            //获取最新能耗值
            float activepowerNow = electricQuantityNowMonth.get(0).getActivepower();
            //获取上月最后一天能耗值
            float activepowerLastDay = electricQuantityNowMonth.get((electricQuantityNowMonth.size() - 1)).getActivepower();
            //本月能耗
            float activepowerNowMonth = activepowerNow-activepowerLastDay;

            //获取上月能耗
            //取出上个月第一天
            String day_first = map.get("first");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date2 = sdf1.parse(day_first);
            statisticsVo.setStartTime(date1);
            statisticsVo.setEndTime(date2);
            //获取上月月能耗所有信息
            List<TElectricQuantity> electricQuantityLastMonth = loopStatusServiceApi.findElectricQuantity(statisticsVo.getDid(),statisticsVo.getHid());
            //获取上月第一天能耗值
            float activepowerFirstDay = electricQuantityLastMonth.get(0).getActivepower();
            //上月能耗
            float activepowerLastMonth = activepowerLastDay-activepowerFirstDay;

            //获取本年能耗




            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }


    /**
     * 根据当前时间得到上个月的第一天和最后一天
     * @param date
     * @return
     */
    private static Map<String, String> getFirstday_Lastday_Month (Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,- 1);
        Date theDate = calendar.getTime();

        // 上个月第一天  
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();
        // 上个月最后一天  
        calendar.add(Calendar.MONTH, 1);// 加一个月  
        calendar.set(Calendar.DATE, 1);// 设置为该月第一天  
        calendar.add(Calendar.DATE, - 1);// 再减一天即为上个月最后一天  
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();

        //获取当前年份第一天


        Map<String, String> map = new HashMap<String, String > ();
        map.put("first",day_first);
        map.put("last",day_last);
        return map;
    }

}
