package com.shuzhi.serviceimpl;

import com.shuzhi.common.utils.Wrapper;
import org.springframework.stereotype.Service;

import com.shuzhi.entity.Role;
import com.shuzhi.service.RoleService;
import com.shuzhi.common.basemapper.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author shuzhi
 * @date 2019-07-04 15:04:42
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Override
    public Wrapper saveRole() {
        return null;
    }
}