package com.shuzhi.controller;

import com.shuzhi.common.utils.Wrapper;
import com.shuzhi.entity.Group;
import com.shuzhi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author shuzhi
 * @date 2019-08-11 11:14:57
 */

@RestController
@RequestMapping(value = "/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * 查询分组信息 传id查单个 不传id查所有
     *
     * @param group 分页信息和设备类型
     * @return 查询结果
     */
    @RequestMapping("/findGroup")
    public Wrapper findGroup(@RequestBody Group group){
        return groupService.findGroup(group);
    }

    /**
     * 删除组id
     *
     * @param id 组id
     * @return 删除结果
     */
    @RequestMapping("/removeGroup/{id}")
    public Wrapper removeGroup(@PathVariable Integer id){
        return groupService.removeGroup(id);
    }

    /**
     * 增加组
     *
     * @param group 组信息
     * @return 增加结果
     */
    @RequestMapping("/addGroup")
    public Wrapper addGroup(@RequestBody Group group){
        return groupService.addGroup(group);
    }

    /**
     * 更新组
     *
     * @param group 组信息
     * @return 更新结果
     */
    @RequestMapping("/updateGroup")
    public Wrapper updateGroup(@RequestBody Group group){
        return groupService.updateGroup(group);
    }

    /**
     * 返回设备列表
     *
     * @param id 设备类型
     * @return 设备列表
     */
    @RequestMapping("/getDeviceMenu/{id}")
    public Wrapper getDeviceMenu(@PathVariable Integer id){
        return groupService.getDeviceMenu(id);
    }
}