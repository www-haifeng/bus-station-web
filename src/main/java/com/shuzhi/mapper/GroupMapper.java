package com.shuzhi.mapper;

import com.shuzhi.entity.DeviceLoop;
import com.shuzhi.entity.Group;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.shuzhi.common.basemapper.MyBaseMapper;

import java.util.List;


/**
 * @author shuzhi
 * @date 2019-08-11 11:14:57
 */

@Repository
public interface GroupMapper extends MyBaseMapper<Group> {

    /**
     * 通过分组id查询分组信息
     *
     * @param id 分组id
     * @param deviceType 设备类型
     * @return 分组信息
     */
    List<DeviceLoop> findById(@Param("id") Integer id, @Param("deviceType") Integer deviceType);

    /**
     * 分组名称下拉框
     *
     * @return 分组名称下拉框
     */
    List<Group> getGroupMenu();
}
