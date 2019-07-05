package com.shuzhi.service;

import com.shuzhi.common.basemapper.BaseService;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * @author shuzhi
 * @date 2019-07-04 15:04:42
 */

public interface UserService extends BaseService<User>, UserDetailsService {

    /**
     * 用户注册
     *
     * @param user 用户注册信息
     * @return 注册结果
     */
    Wrapper registered(User user);
}