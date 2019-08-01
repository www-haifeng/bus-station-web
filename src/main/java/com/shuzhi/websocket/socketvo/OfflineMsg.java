package com.shuzhi.websocket.socketvo;

import com.shuzhi.lcd.entities.IotLcdStatusTwo;
import com.shuzhi.led.entities.TStatusDto;
import com.shuzhi.light.entities.TLoopStateDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zgk
 * @description
 * @date 2019-08-01 9:11
 */
@Data
public class OfflineMsg {

    List<OfflineVo> offlines = new ArrayList<>();

    public void offlineLightMsg(List<TLoopStateDto> loopStatus) {

        loopStatus.forEach(loopStateDto -> {
            OfflineVo offlineVo = new OfflineVo(loopStateDto);
            offlines.add(offlineVo);
        });

    }

    public void offlineLcdMsg(List<IotLcdStatusTwo> allStatusByRedis) {

        allStatusByRedis.forEach(iotLcdStatusTwo -> {
            OfflineVo offlineVo = new OfflineVo(iotLcdStatusTwo);
            offlines.add(offlineVo);
        });


    }

    public void offlineLedMsg(List<TStatusDto> allStatus) {

        allStatus.forEach(tStatusDto -> {
            OfflineVo offlineVo = new OfflineVo(tStatusDto);
            offlines.add(offlineVo);
        });

    }
}
