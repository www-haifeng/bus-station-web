package com.shuzhi.service;

import com.shuzhi.common.basemapper.BaseService;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Role;



/**
 * @author shuzhi
 * @date 2019-07-04 15:04:42
 */

public interface RoleService extends BaseService<Role> {

    /**
     * 保存角色
     *
     * @return 保存结果
     */
    Wrapper saveRole();
}