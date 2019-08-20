package com.shuzhi.service;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Bus;
import com.shuzhi.common.basemapper.BaseService;


/**
 * @author shuzhi
 * @date 2019-08-20 15:32:51
 */

public interface BusService extends BaseService<Bus> {

    /**
     * 获取公交站列表信息
     *
     * @return 公交站列表
     */
    Wrapper findBusStation();
}