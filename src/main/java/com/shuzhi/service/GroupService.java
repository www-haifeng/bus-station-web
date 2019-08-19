package com.shuzhi.service;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Group;
import com.shuzhi.common.basemapper.BaseService;


/**
 * @author shuzhi
 * @date 2019-08-11 11:14:57
 */

public interface GroupService extends BaseService<Group> {

    /**
     * 查询分组信息 传id查单个 不传id查所有
     *
     * @param group 分页信息和设备类型
     * @return 查询结果
     */
    Wrapper findGroup(Group group);

    /**
     * 删除组id
     *
     * @param id 组id
     * @return 删除结果
     */
    Wrapper removeGroup(Integer id);

    /**
     * 增加组
     *
     * @param group 组信息
     * @return 增加结果
     */
    Wrapper addGroup(Group group);

    /**
     * 更新组
     *
     * @param group 组信息
     * @return 更新结果
     */
    Wrapper updateGroup(Group group);

    /**
     * 返回设备列表
     *
     * @return 设备列表
     * @param id 设备类型
     */
    Wrapper getDeviceMenu(Integer id);

    /**
     * 分组名称下拉框
     *
     * @return 分组名称下拉框
     */
    Wrapper getGroupMenu();
}