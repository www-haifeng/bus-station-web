package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Role;
import com.shuzhi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author shuzhi
 * @date 2019-07-04 15:04:42
 */

@RestController
@RequestMapping(value = "/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 保存角色
     *
     * @param role 角色信息
     * @return 保存结果
     */
    @RequestMapping("/saveRole")
    public Wrapper saveRole(Role role){
        return roleService.saveRole(role);
    }
}