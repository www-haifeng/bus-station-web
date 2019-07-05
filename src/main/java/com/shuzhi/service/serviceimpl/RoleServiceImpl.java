package com.shuzhi.service.serviceimpl;

import com.shuzhi.common.utils.WrapMapper;
import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.User;
import com.shuzhi.function.Validation;
import com.shuzhi.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuzhi.entity.Role;
import com.shuzhi.service.RoleService;
import com.shuzhi.common.basemapper.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import static com.shuzhi.eum.LoginEum.*;


/**
 * @author shuzhi
 * @date 2019-07-04 15:04:42
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }


    @Override
    public Wrapper saveRole(Role role) {
        //验证参数
        if (validation().check(role) != null) {
            return validation().check(role);
        }
        return null;
    }

    /**
     * 验证参数
     *
     * @return 验证结果
     */
    private Validation<Role> validation() {
        Role roleSelcet = new Role();
        return role -> {
            //判断角色名是否重复
            roleSelcet.setRoleName(role.getRoleName());
            if (StringUtils.isBlank(role.getRoleName()) ) {
                return WrapMapper.wrap(ROLE_ERROR_1.getCode(), ROLE_ERROR_1.getMsg());
            }
            if (roleMapper.selectOne(roleSelcet) != null){
                return WrapMapper.wrap(ROLE_ERROR_2.getCode(), ROLE_ERROR_2.getMsg());
            }

            //判断角色编号是否重复
            if (StringUtils.isBlank(role.getRoleCode()) ) {
                return WrapMapper.wrap(ROLE_ERROR_3.getCode(), ROLE_ERROR_3.getMsg());
            }
            roleSelcet.setRoleName(null);
            roleSelcet.setRoleCode(role.getRoleCode());
            if (roleMapper.selectOne(roleSelcet) != null){
                return WrapMapper.wrap(ROLE_ERROR_4.getCode(), ROLE_ERROR_4.getMsg());
            }
            return null;
        };
    }
}