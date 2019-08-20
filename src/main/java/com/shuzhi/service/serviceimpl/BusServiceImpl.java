package com.shuzhi.service.serviceimpl;

import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.mapper.BusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuzhi.entity.Bus;
import com.shuzhi.service.BusService;
import com.shuzhi.common.basemapper.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author shuzhi
 * @date 2019-08-20 15:32:51
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class BusServiceImpl extends BaseServiceImpl<Bus> implements BusService {

    private final BusMapper busMapper;

    public BusServiceImpl(BusMapper busMapper) {
        this.busMapper = busMapper;
    }

    /**
     * 获取公交站列表信息
     *
     * @return 公交站列表
     */
    @Override
    public Wrapper findBusStation() {
        return WrapMapper.handleResult(busMapper.findBusStation());
    }
}