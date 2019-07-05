package com.shuzhi.serviceimpl;

import org.springframework.stereotype.Service;

import com.shuzhi.entity.UserRole;
import com.shuzhi.service.UserRoleService;
import com.shuzhi.common.basemapper.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author shuzhi
 * @date 2019-07-04 15:04:42
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {

}