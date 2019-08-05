package com.shuzhi.websocket.socketvo;

import lombok.Data;

import java.util.List;

/**
 * @author zgk
 * @description 用电管理返回信息
 * @date 2019-08-04 10:19
 */
@Data
public class GatewaysMsg {

    private List<Gateways> gateways;

    public GatewaysMsg(List<Gateways> gateways) {
        this.gateways = gateways;
    }
}
