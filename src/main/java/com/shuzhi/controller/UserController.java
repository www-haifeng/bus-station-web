package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.User;
import com.shuzhi.function.Validation;
import com.shuzhi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户登录注册等
 *
 * @author shuzhi
 * @date 2019-07-04 15:04:42
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/test")
    public String registered() {
        return "OK";
    }
}