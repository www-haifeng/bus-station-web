package com.shuzhi.websocket.socketvo;

import lombok.Data;

/**
 * @author zgk
 * @description 照明首次连接数据
 * @date 2019-07-15 16:30
 */
@Data
public class LightMsg {

    private Lights lights;

    public LightMsg(Lights lights) {
        this.lights = lights;
    }
}
