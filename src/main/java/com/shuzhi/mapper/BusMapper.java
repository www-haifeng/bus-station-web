package com.shuzhi.mapper;

import com.shuzhi.entity.Bus;
import com.shuzhi.entity.vo.BusStationVo;
import org.springframework.stereotype.Repository;
import com.shuzhi.common.basemapper.MyBaseMapper;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;


/**
 * @author shuzhi
 * @date 2019-08-20 15:32:51
 */

@Repository
public interface BusMapper extends MyBaseMapper<Bus> {

    /**
     * 获取公交站列表信息
     *
     * @return 公交站列表
     */
    List<BusStationVo> findBusStation();

}
