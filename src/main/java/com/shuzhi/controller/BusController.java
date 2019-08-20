package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author shuzhi
 * @date 2019-08-20 15:32:51
 */

@RestController
@RequestMapping(value = "/bus")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    /**
     * 获取公交站列表信息
     *
     * @return 公交站列表
     */
    @RequestMapping("/findBusStation")
    public Wrapper findBusStation(){
        return busService.findBusStation();
    }
}