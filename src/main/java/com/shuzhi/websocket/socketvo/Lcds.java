package com.shuzhi.websocket.socketvo;

import com.shuzhi.lcd.entities.IotLcdStatus;
import lombok.Data;

import java.util.List;

/**
 * @author zgk
 * @description
 * @date 2019-07-18 17:04
 */
@Data
public class Lcds {

    private List<IotLcdStatus> allStatusByRedis;

    public Lcds(List<IotLcdStatus> allStatusByRedis) {
        this.allStatusByRedis = allStatusByRedis;
    }
}
